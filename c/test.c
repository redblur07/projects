#include <stdio.h>

void ohyes();

int main(int argc, char **argv) {
	

	FILE *fp;

	fp = fopen("tmp/test.txt", "w+");
	fprintf(fp, "This is testing for fprintf...\n");
	fputs("This is testing for fputs...\n", fp);
	fclose(fp);

	return 0;
}

void ohyes(){
	printf("ohyes \n");
}
