#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <errno.h>
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <netdb.h>

#define MAX_BUFFER_LENGTH 32

char *itob(int n){
	// init vars
	int c, d, count;
	char *pointer;
	count = 0;

	// set bytes to 0 
	pointer = (char*)malloc(MAX_BUFFER_LENGTH + 1);

	if (pointer == NULL)
		exit(EXIT_FAILURE);

	for (c = MAX_BUFFER_LENGTH - 1; c >= 0; c--)
	{
		// shift it to the right to get the last digit
		d = n >> c;

		// if the last binary matches a 1 than add 1 to the pointer, else 0
		if (d & 1)
			*(pointer + count) = '1';
		else
			*(pointer + count) = '0';

		count++;
	}

	// terminate the string
	*(pointer + count) = '\0';

	return  pointer;
}

int main(int argc, char *argv[])
{
	char host[16];
	int port;

	// Get Server Data
	printf("Enter the Server Address: ");
	scanf("%s", &host);
	printf("Enter port to connect to: ");
	scanf("%d", &port);
	printf("%s:%d\n\n", host, port);

	// Get the number to send
	int n;
	printf("Please enter a number to send to the server: ");
	scanf("%d", &n);



	//create socket
	int sock;
	struct sockaddr_in server;
	struct hostent *he;
	if ((sock = socket(PF_INET, SOCK_STREAM, IPPROTO_TCP)) < 0){
		printf("error at creating socket \n");
		exit(1);
	}

	he = gethostbyname(host);
	if (he == NULL) { 
		herror("gethostbyname");
		exit(1);
	}


	//setup transport address
	memset(&server, 0, sizeof(server));
	server.sin_family = AF_INET;
	server.sin_port = htons(port);
	server.sin_addr = *((struct in_addr *)he->h_addr);



	//connect
	if (connect(sock, (struct sockaddr *) &server, sizeof(server)) < 0){
		printf("error! could not connect to %s:%d\n\n",host,port);
		exit(1);
	}


	//send
	char *pointer;
	pointer = itob(n);
	printf("sending number %d as binary %s\n\n",n,pointer);
	send(sock, pointer, MAX_BUFFER_LENGTH, 0);
	printf("msg send\n\n");


	//close socket
	getchar();
	close(sock);
	return 0;
}