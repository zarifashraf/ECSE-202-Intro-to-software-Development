    /*
 ============================================================================
 Name        : database.c
 Author      : zedzeeden
 Version     :
 Copyright   : Your copyright notice

 Description : The purpose of this assignment is to
build a very simple database program using B-Trees for record storage. It uses 2 files, NamesIDs.txt and marks.txt, containing student names and IDs in
the First file and the corresponding grades in the second. The program runs a simple cmd interpreter using the
following 2-letter commands:
LN List all the records in the database ordered by Last name.
LI List all the records in the database ordered by student ID.
FN Prompts for a name and lists the record of the student with the
corresponding name.
FI Prompts for a name and lists the record of the student with the
Corresponding ID.
HELP Prints this list
? Prints this list
QUIT Exits the program.
 ============================================================================
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>

#define MAXLEN 20
#define false 0
#define true !false

// Structure Templates

struct SR{
	char First [MAXLEN];
	char Last [MAXLEN];
	int ID;
	int marks;
	struct SR *left;
	struct SR *right;
};

// Function Prototypes

void addNode (struct SR** root , struct SR* input, int type)
{

	//if root is empty
	if((*root) == NULL) {

		//create a root with input value
		(*root) = (struct SR*)malloc(sizeof(struct SR));

		(*root)->ID = input->ID;
		(*root)->marks = input->marks;

		//Copy the string

		strcpy((*root)->First,input->First);
		strcpy((*root)->Last,input->Last);

		(*root)->left = NULL;
		(*root)->right = NULL;
	}

	else {
		//if type is 1, then sort based on student ID
		//if type is not 1, then sort based on last names

		if(type == 1) {

			//compare student ID
			if(input->ID < (**root).ID)
			{
				//Insert to left tree
				addNode(&((*root)->left), input, type);
			}else
			{
				//Insert to right tree
				addNode(&((*root)->right), input, type);
			}
		}
		else {
			//compare Last names
			if(strcmp(input->Last, (**root).Last)<0)
			{

				//Insert to left tree
				addNode(&((*root)->left), input, type);
			}
			else {

				addNode(&((*root)->right), input, type);
			}
		}
	}
}

//code to inorder the tree
void inorder(struct SR* root)
{
	//if there is left tree inorder, left
	if(root->left != NULL)
	{
		inorder(root->left);
	}
	char string [30];
	strcpy(string, root->First);
	strcat(string, " ");
	strcat(string, root->Last);

	printf("%-20s %-10d %-5d\n", string , root->ID, root->marks);

	if(root->right != NULL)
	{
		inorder(root->right);
	}
}

int search;

	void search_Name(struct SR* bt, char* str){ // search function for names

		if(bt->left != NULL){
			search_Name(bt->left,str);
}

		{

			if(strcasecmp(str, bt->Last)==0){
				search=1;
				printf("Student Name:    %s %s \n", bt->First, bt->Last);
				printf("Student ID:      %d \n", bt->ID);
				printf("Total Grade:     %d \n", bt->marks);
			}


		}

		if(bt->right != NULL){
			search_Name(bt->right,str);

		}


	}

	void search_ID(struct SR* bt, int num){ //search function for IDs

		if(bt->left != NULL){
			search_ID(bt->left,num);

		}

		{

			if(bt->ID==num){
				search=1;
				printf("Student Name:    %s %s \n", bt->First, bt->Last);
				printf("Student ID:      %d \n", bt->ID);
				printf("Total Grade:     %d \n", bt->marks);
			}


		}

		if(bt->right != NULL){
			search_ID(bt->right,num);

		}

	}

// Main entry point is here.  Program uses the standard Command Line Interface

int main(int argc, char* argv[])
{

	// Internal declarations
	struct SR Record, *root_N, *root_I;
	FILE *NamesIDs;
	FILE *marks;

	// Argument check
	 if (argc != 3) {
		 printf("Usage: sdb [Names+IDs] [marks] \n");
	     return -1;
	 }

	 // Attempt to open the user-specified file.  If no file with the supplied name is found, exit the program with an error message.
	if((NamesIDs = fopen(argv[1], "r")) == NULL) { //open Names IDs file
		printf("Can't read from file %s\n",argv[1]);
		return -2;
	}
	if ((marks = fopen(argv[2],"r")) == NULL) { // open marks file
		printf("Can't read from file %s\n",argv[2]);
		return -2;
	}
	// Initialize B-Trees by creating the root pointers;

	root_N=NULL;
	root_I=NULL;

	//  Read through the NamesIDs and marks files, record by record.
	int NumRecords=0;
	printf("Building database...\n");

	while (fscanf(NamesIDs,"%s%s%d", // scan record into structure
			&(Record.First[0]),
			&(Record.Last[0]),
			&(Record.ID)) != EOF)
	{
		fscanf(marks,"%d",&(Record.marks)); // marks too
		NumRecords++;
		addNode(&root_N,&Record, 0); // copy to B-Tree sorted by Last
		if (root_N==NULL) {
			printf("Failed to allocate object for Record in main\n");
			return -3;
		}
		addNode(&root_I,&Record, 1); // copy to B-Tree sorted by Last
		if (root_I==NULL) {
			printf("Failed to allocate object for Record in main\n");
			return -4;
		}
	}

	// Close files once we're done

	fclose(NamesIDs);
	fclose(marks);

	printf("Finished, %d records found...\n",NumRecords);


	int Exit=0;

	char cmd[MAXLEN];

		while(Exit ==0){ //asks for user input
			printf(" \n");
			printf("sdb: ");
			scanf("%s",cmd);

			//strcasecmp() --> case Insensitive
			// List by Name

			if(strcasecmp(cmd,"LN")==0){ // List all records sorted by name
				if(root_N==NULL){
					printf("RootName is NULL");
				}else{
					printf("Student Record Database sorted by Last Name\n");
					printf(" \n");
					inorder(root_N);
				}

			}

			// List by ID
			else if(strcasecmp(cmd,"LI")==0){ // List all records sorted by ID
				if(root_N==NULL){
					printf("RootID is NULL");
				}else{
					printf("Student Record Database sorted by ID\n");
					printf(" \n");
					inorder(root_I);
				}

			}

			// Find record that matches Name
			else if(strcasecmp(cmd,"FN")==0){ // List record that matches name
				search=0;

				printf(" \n");
				printf("Enter name to search: ");
				scanf("%s",cmd);
				search_Name(root_N,cmd);
				if(search==0){
					printf("There is no student with that name.");
					printf(" \n");
				}


			}

			// Find record that matches ID
			else if(strcasecmp(cmd,"FI")==0){ // // List record that matches ID
				search=0;
				int num_search;
				printf(" \n");
				printf("Enter ID to search: ");
				scanf("%d",&num_search);
				search_ID(root_I,num_search);
				if(search==0){
					printf("There is no student with that ID.");
					printf(" \n");
				}


			}

			// Print the list of functions triggered by the commands
			else if((strcasecmp(cmd,"HELP")==0)||(strcasecmp(cmd,"?")==0)){  //if user inputs ? or if the user inputs HELP
				printf("LN      List all the records in the database ordered by Last name.\n");
				printf("LI      List all the records in the database ordered by student ID.\n");
				printf("FN      Prompts for a name and lists the record of the student with the corresponding name.\n");
				printf("F1      Prompts for a name and lists the record of the student with the Corresponding ID. \n");
				printf("HELP    Prints this list\n");
				printf("?       Prints this list\n");
				printf("Q       Exits the program.\n");

			}

			// Ends the program
			else if(strcasecmp(cmd,"Q")==0 || (strcasecmp(cmd,"QUIT")==0)){ // if user inputs Q or QUIT
				printf("Program terminated...");
				Exit=1;

			}

			// If the user inputs a wrong command
			else{
				printf("Command not understood.\n");
			}


		}

		return EXIT_SUCCESS;
}






