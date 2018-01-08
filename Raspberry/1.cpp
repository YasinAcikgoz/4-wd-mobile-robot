 #include <stdio.h>
#include <stdlib.h>
#include<wiringPi.h>
#include <softPwm.h>
#include <signal.h>
#include <unistd.h>
#include <pthread.h>
#include <signal.h>
#include <sys/msg.h>
#include <dirent.h>
#include <sys/stat.h>
#include <time.h>
#include <sys/types.h>
#include <limits.h>
#include <sys/ipc.h>
#include <sys/time.h>
#include <string.h>
#include <ifaddrs.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <time.h>
#include <fcntl.h>
#include <netdb.h>
#include <sys/socket.h>
#include <net/if.h>
#include "rs232.h"
#include "I2Cdev.h"

#define IP_MAX 20

//4 - dogru
#define motorPinLF1 18
#define motorPinLF2 17
#define enablePinLF 4
//2
#define motorPinRF1 16 //23
#define motorPinRF2 13 //22
#define enablePinRF 26 //27

//1
#define motorPinRR1 12
#define motorPinRR2 6
#define enablePinRR 5

//3
#define motorPinLR1 22
#define motorPinLR2 23
#define enablePinLR 27


int socketFD, listenfd, port;
int err, pwm;

char mode[] = { '8', 'N', '1', 0 };
int  bdrate = 9600;
int cport_nr = 24;
pthread_t tid, tid2;
volatile int flag = 1;
int running;
void stop();
int k = 5;
void move(int pwm, int del,  char c, int enable, int m1, int m2);
int calibrateFinish = 0;
int sock;
void checkMotors();
void gyro();
int establish (unsigned short portnum);

void makeMove(int pwm, char direction, int enable, int m1, int m2);
void calibrateMotors();
void initilizeMotors();
void readCommand();
void *readFromSerial(void *);


void *readFromPhone(void *);

/* error bilgisini ekrana basan fonksiyon */
void err_sys(const char *x){ 
	perror(x);
	exit(-1);
}

void intHandler(int a) {
	printf("CTRL-C Handler\n");
	stop();
	char arr[16];
	strcpy(arr, "eeeeeeeeeeeeeeee");
	if(write(socketFD, &arr, sizeof(arr))<0)
		err_sys("write");
	close(socketFD);
	close(sock);
	close(port);
//	RS232_CloseComport(cport_nr );
	exit(0);
}

int main(int argc, char *argv[])
{
	char c;
	system("clear");/*
    printf("Initializing I2C devices...\n");
    accelgyro.initialize();

    // verify connection
    printf("Testing device connections...\n");
    printf(accelgyro.testConnection() ? "MPU6050 connection successful\n" : "MPU6050 connection failed\n");
	*/signal(SIGINT, intHandler);

	if(wiringPiSetupGpio()==-1)
		err_sys("Setup wiring pi failed");


	port=atoi(argv[1]);
    fprintf(stderr, "Server Started: %d\n", port);
	if((listenfd = establish(port))<0)
		err_sys("socket");
	while(1){
		
		if((socketFD = accept(listenfd, NULL, NULL))<0)
			err_sys("accept");
		else{
			printf("baglandi\n");
			pthread_create(&tid2, NULL, &readFromPhone, NULL);
			perror("pthread_create2");
			

			
		}
		initilizeMotors();
		calibrateMotors();
	}
	//			initilizeMotors();
		//		calibrateMotors();
		/*makeMove(100, 'f', enablePinLF, motorPinLF1, motorPinLF2);
		makeMove(100, 'f', enablePinRF, motorPinRF1, motorPinRF2);
		makeMove(100, 'f', enablePinLR, motorPinLR1, motorPinLR2);
		makeMove(100, 'f', enablePinRR, motorPinRR1, motorPinRR2);
		delay(20000);*/
		/*	int i = 0;
			for(i =0; i<100; ++i){
		      	move(100,100, 'f', enablePinLF, motorPinLF1, motorPinLF2);
		      	printf("%d\n", i);
			
			}*/
		      	
		      	/*stop();
		      	delay(1000);*/
		      //	move(100,2000, 'r', enablePinLF, motorPinLF1, motorPinLF2);
		 /*     	stop();
		      	delay(1000);*/
	
	return 0;
}
void calibrateMotors(){
	if (RS232_OpenComport(cport_nr, bdrate, mode))
		err_sys("Can not open comport");
	if(RS232_SendByte(cport_nr, 's')){
		err_sys("SendByte");
		
	}
	else{
		makeMove(100, 'f', enablePinLF, motorPinLF1, motorPinLF2);
		makeMove(100, 'f', enablePinRF, motorPinRF1, motorPinRF2);
		makeMove(100, 'f', enablePinLR, motorPinLR1, motorPinLR2);
		makeMove(100, 'f', enablePinRR, motorPinRR1, motorPinRR2);
		delay(3000);
		
		stop();
		delay(1000);
		
		makeMove(100, 'r', enablePinLF, motorPinLF1, motorPinLF2);
		makeMove(100, 'r', enablePinRF, motorPinRF1, motorPinRF2);
		makeMove(100, 'r', enablePinLR, motorPinLR1, motorPinLR2);
		makeMove(100, 'r', enablePinRR, motorPinRR1, motorPinRR2);
		delay(3000);
		
		stop();
		delay(1000);
		
		
		printf("calibrate motors\n");


		unsigned char c[1];
		while(1){
		    fprintf(stderr, "döngü\n");
			int n = RS232_PollComport(cport_nr, c ,1);
			if(n == 1 && c[0] == 's'){
				fprintf(stderr, "handshake\n");
				running = 1;
				calibrateFinish = 1;
				pthread_create(&tid, NULL, &readFromSerial, NULL);
				perror("pthread_create1");
			
				   char direct = 'f';
			/*		makeMove(100, direct, enablePinLF, motorPinLF1, motorPinLF2);
					makeMove(100, direct, enablePinRF, motorPinRF1, motorPinRF2);
					makeMove(100, direct, enablePinLR, motorPinLR1, motorPinLR2);
					makeMove(100 , direct, enablePinRR, motorPinRR1, motorPinRR2);
                delay(30000);	
                   stop();*/
				/*
				pthread_create(&tid, NULL, &readFromSerial, NULL);
				perror("pthread_create1");
				printf("calibrateFinish\n");*/
				break; 
				
				
			}
		}
	}

}
void move(int pwm, int del, char c, int enable, int m1, int m2){
        
	makeMove(pwm,c, enable, m1, m2);
	delay(del);
	running = 0;
	stop();
	delay(1000);
/*
	makeMove(pwm, 'r', enable, m1, m2);
	delay(del);
	stop();
	delay(1000);*/

}
void *readFromPhone(void *dummy){
	fprintf(stderr, "phone thread start: %d\n", socketFD);
	int  direction, yon;

	char direct;
	char arr[4];
	while(1){
		if(calibrateFinish == 1){
			read(socketFD, &arr, sizeof(arr));
			fprintf(stderr, "pwm -> %d\n", pwm);
			
			
			direction = arr[0] - '0';
			pwm = arr[1]- '0';
			yon = arr[2]- '0';
			running = arr[3]- '0';
			
			if(running == 1){
				if(yon == 1){
					direct = 'r';
				} else
				    direct = 'f';
				if(direction == 1 && pwm == 0){
				//	fprintf(stderr, "düz\n");
					makeMove(50, direct, enablePinLF, motorPinLF1, motorPinLF2);
					makeMove(40, direct, enablePinRF, motorPinRF1, motorPinRF2);
					makeMove(40,direct, enablePinRR, motorPinRR1, motorPinRR2);
					makeMove(50, direct, enablePinLR, motorPinLR1, motorPinLR2);

				}
				else if(direction == 1){
					fprintf(stderr, "sağa dön: %d\n", pwm*k);
					makeMove(40, direct, enablePinLF, motorPinLF1, motorPinLF2);
					makeMove(40, direct, enablePinLR, motorPinLR1, motorPinLR2);
					makeMove((10-pwm)*k, direct, enablePinRF, motorPinRF1, motorPinRF2);
					makeMove((10-pwm)*k, direct, enablePinRR, motorPinRR1, motorPinRR2);
				} else if (direction == 2){
					fprintf(stderr, "sola dön: %d\n", pwm*k);
					makeMove((10-pwm)*k, direct, enablePinLF, motorPinLF1, motorPinLF2);
					makeMove((10-pwm)*k, direct, enablePinLR, motorPinLR1, motorPinLR2);
					makeMove(40, direct, enablePinRF, motorPinRF1, motorPinRF2);
					makeMove(40, direct, enablePinRR, motorPinRR1, motorPinRR2);
				}
				
			} else {
			//	stop();           
					makeMove(0, direct, enablePinLF, motorPinLF1, motorPinLF2);
					makeMove(0, direct, enablePinLR, motorPinLR1, motorPinLR2);
					makeMove(0, direct, enablePinRF, motorPinRF1, motorPinRF2);
					makeMove(0, direct, enablePinRR, motorPinRR1, motorPinRR2);
				
			}
		}
	}
	
	
}
void *readFromSerial(void *dummy){
	int n=0;
	unsigned char buf[20];
	char *null;
	char arr[3], tokens[4][3], *temp , str[20], sendstr[36];
	int pwm, i=0, pulses[4], plsLF =0, plsRF =0, plsRR =0, plsLR =0, count=0, size = -1;
	int  vLF1 =0, vLF2=0, vLR1=0, vLR2=0, vRF1=0, vRF2=0, vRR1=0, vRR2=0, vRF=0, vLR=0, vLF=0, vRR=0;
    
	while (1)
	{

		if(running == 1){
		n = RS232_PollComport(cport_nr, buf,  sizeof(buf));
		//fprintf(stderr, "buf: %s\n", buf );
		if (n == 20){
			write(socketFD, &buf, sizeof(buf));
			//strcpy(buf, "\0");
           // gyro();
	
		  }
		RS232_flushRX(cport_nr);
		delay(200);       
		}
		
	}

}
void initilizeMotors(){

	pinMode(motorPinLF1, OUTPUT);
	pinMode(motorPinLF2, OUTPUT);
	pinMode(enablePinLF, PWM_OUTPUT);

	pinMode(motorPinLR1, OUTPUT);
	pinMode(motorPinLR2, OUTPUT);
	pinMode(enablePinLR, PWM_OUTPUT);

	pinMode(motorPinRF1, OUTPUT);
	pinMode(motorPinRF2, OUTPUT);
	pinMode(enablePinRF, PWM_OUTPUT);

	pinMode(motorPinRR1, OUTPUT);
	pinMode(motorPinRR2, OUTPUT);
	pinMode(enablePinRR, PWM_OUTPUT);

	softPwmCreate(enablePinLR,0,100);
	softPwmCreate(enablePinLF,0,100);
	softPwmCreate(enablePinRF,0,100);
	softPwmCreate(enablePinRR,0,100);       
}
void checkMotors(){
	while(1){
		printf("sol\n");
		
		
		makeMove(100, 'f', enablePinLF, motorPinLF1, motorPinLF2);
		makeMove(100, 'f', enablePinLR, motorPinLR1, motorPinLR2);
		makeMove(100, 'f', enablePinRF, motorPinRF1, motorPinRF2);
		makeMove(100, 'f', enablePinRR, motorPinRR1, motorPinRR2);
		delay(5000);
		
		printf("dur\n");
		stop();
		delay(1000);
		
		printf("sag\n");
		makeMove(100, 'r', enablePinLF, motorPinLF1, motorPinLF2);
		makeMove(100, 'r', enablePinLR, motorPinLR1, motorPinLR2);
		makeMove(100, 'r', enablePinRF, motorPinRF1, motorPinRF2);
		makeMove(100, 'r', enablePinRR, motorPinRR1, motorPinRR2);
		delay(5000);
		
		printf("dur\n");
		stop();
		delay(5000);
	}
	
}
void makeMove(int pwm, char direction, int enable, int m1, int m2){
	
	if(direction == 'f'){
		softPwmWrite(enable ,pwm);
		digitalWrite(m2,LOW);
		digitalWrite(m1,HIGH);
		
	} else if(direction == 'r'){
		softPwmWrite(enable,pwm);
		digitalWrite(m1,LOW);
		digitalWrite(m2,HIGH);
	}   
	
}
void stop(){

	digitalWrite(motorPinRF1,LOW);
	digitalWrite(motorPinRF2,LOW);
	softPwmWrite(enablePinRF ,0);
	
	digitalWrite(motorPinLF1,LOW);
	digitalWrite(motorPinLF2,LOW);
	softPwmWrite(enablePinLF ,0);
	
	digitalWrite(motorPinLR1,LOW);
	digitalWrite(motorPinLR2,LOW);
	softPwmWrite(enablePinLR ,0);
	
	digitalWrite(motorPinRR1,LOW);
	digitalWrite(motorPinRR2,LOW);
	softPwmWrite(enablePinRR ,0);

}
int establish (unsigned short portnum){
	char myname[IP_MAX];
	int s;
	struct sockaddr_in serverAdress;
	struct hostent *hp;

	memset(&serverAdress, 0, sizeof(struct sockaddr_in));
	gethostname(myname,IP_MAX);

	serverAdress.sin_family= AF_INET;
	serverAdress.sin_port= htons(portnum);
	serverAdress.sin_addr.s_addr=htonl(INADDR_ANY);
	
	if ((s= socket(AF_INET, SOCK_STREAM, 0)) < 0)
		return -1;
	if (bind(s, (struct sockaddr *) &serverAdress,sizeof(struct sockaddr_in)) < 0){
		close(s);
		return -1;
	}
	listen(s, 10);
	return s;
}