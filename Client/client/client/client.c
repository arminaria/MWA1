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
	int c, d, count;
	char *pointer;

	count = 0;
	pointer = (char*)malloc(MAX_BUFFER_LENGTH + 1);

	if (pointer == NULL)
		exit(EXIT_FAILURE);

	for (c = MAX_BUFFER_LENGTH-1; c >= 0; c--)
	{
		d = n >> c;
		printf("%d\n", d);

		if (d & 1)
			*(pointer + count) = 1;
		else
			*(pointer + count) = 0;

		count++;
	}
	*(pointer + count) = '\0';

	return  pointer;
}

int main(int argc, char *argv[])
{
	
	
	int n, c, k;
	char *pointer;

	printf("Enter an integer in decimal number system\n");
	scanf("%d", &n);

	pointer = itob(n);
	printf("Binary string of %d is: %s\n", n, pointer);

	free(pointer);

	getchar();
	return 0;

	int sock;
	struct sockaddr_in server;
	int port = 12345;
	char *serverIP = "127.0.0.1";
	char *msg = "1235567";
	int num = 666;
	struct hostent *he;

	printf("client start\n\n");
	printf("%i\n\n",num);
	int num2 = htons(num);
	printf("%i\n\n",num2);

	//create socket

	if( 0 > (sock = socket(PF_INET, SOCK_STREAM, IPPROTO_TCP))){
		printf("error at creating\n\n");
		exit(1);
		}
	
	if ((he=gethostbyname(serverIP)) == NULL) {  // get the host info
        herror("gethostbyname");
        exit(1);
	}


	//setup transport address
	memset(&server, 0, sizeof(server));
	server.sin_family = AF_INET;     
	server.sin_port = htons(port);
	server.sin_addr = *((struct in_addr *)he->h_addr);



	//connect
	if(connect(sock, (struct sockaddr *) &server, sizeof(server)) < 0){
		printf("error at connect\n\n");
		exit(1);	
	}
	
    	
	//send
	char *msg2 = (char*) &num2;
	printf("%s\n\n",msg2);

	
	
	send(sock, msg, sizeof(msg) ,0);
	printf("msg send\n\n");
	printf("%s \n",msg);

	getchar();

	close(sock);
	

    return 0;
}