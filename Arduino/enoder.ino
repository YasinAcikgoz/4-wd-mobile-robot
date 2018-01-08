#include <Encoder.h>

long oldPosition  = 0;
long oldPosition2  = 0;
long oldPosition3  = 0;
long oldPosition4  = 0;
int init1max, init2max, init1min, init2min, init3max, init3min, init4max, init4min;
boolean flag, flag2;
int count = 0;

// Change these two numbers to the pins connected to your encoder.
//   Best Performance: both pins have interrupt capability
//   Good Performance: only the first pin has interrupt capability
//   Low Performance:  neither pin has interrupt capability

Encoder myEnc(2,5);
Encoder myEnc2(3,6);
Encoder myEnc3(18,7);
Encoder myEnc4(19,8);

long Time1 = 0, Time2 = 0, Time3 = 0;

void setup() {
  Serial.begin(9600);
  flag = true;
  flag2 = false;
}


void loop() {
 while(flag){
    if(Serial.available() > 0){
    char c = Serial.read();
    if(c == 's'){
        flag = false;
        flag2 = true;
         Serial.end();
    }
  }
 }
  
  Time1 = millis();
  Time3 = Time1 -= Time2;
  
  if(Time3 > 200){
    
    Time2 = millis();
   
      
    if(flag2){
        int t1 = getPos1(false);
        int t2 = getPos2(false);
        int t3 = getPos3(false);
        int t4 = getPos4(false);
      /*  
        Serial.print("t1: ");
        Serial.print(t1);
        Serial.print(" t2: ");
        Serial.print(t2);
        Serial.print(" t3: ");
        Serial.print(t3);
        Serial.print(" t4: ");
        Serial.print(t4);
        Serial.println();*/
        
        if(t1>0 && t1>init1max)
          init1max = t1;
        if(t2>0 && t2>init2max)
          init2max = t2;
        if(t3>0 && t3>init3max)
          init3max = t3;
        if(t4>0 && t4>init4max)
          init4max = t4;
          
        if(t1<0 && t1<init1min)
          init1min = t1;
        if(t2<0 && t2<init2min)
          init2min = t2;
        if(t3<0 && t3<init3min)
          init3min = t3;
        if(t4<0 && t4<init4min)
          init4min = t4;
  
          
        if(count >= 50){
        
        /*  Serial.print("i1max: ");
          Serial.print(init1max);
          Serial.print(" i1min: ");
          Serial.println(init1min);
          Serial.print("i2max: ");
          Serial.print(init2max);
          Serial.print(" i2min: ");
          Serial.println(init2min);
          Serial.print("i3max: ");
          Serial.print(init3max);
          Serial.print(" i3min: ");
          Serial.println(init3min);
          Serial.print("i4max: ");
          Serial.print(init4max);
          Serial.print(" i4min: ");
          Serial.println(init4min);*/
          oldPosition  = 0;
          oldPosition2  = 0;
          oldPosition3  = 0;
          oldPosition4  = 0;
          Serial.begin(9600);
          delay(100);
          Serial.print('s');
          flag2 = false;
        }
        else
          ++count;
      
    } else{
        int a = getPos1(true);
        int b = getPos2(true);
        int c = getPos3(true);
        int d = getPos4(true);
        if(a == 0)
          a = 3000;
        if(b == 0)
          b = 3000;
        if(c == 0)
          c = 3000;
        if(d == 0) 
          d = 3000;
        
        char arr[20];
        sprintf(arr, "%d_%d_%d_%d_", a,b,c,d);
        Serial.print(arr);
        Serial.flush();
        
    }
  }
}
 
long getPos1(boolean status){
  
long newPosition = myEnc.read();
  if (newPosition != oldPosition) {
     long r;
    if(status)
       r = map((newPosition - oldPosition),init1max, init1min,  2000, 4000);
    else
      r = newPosition - oldPosition;
    oldPosition = newPosition;
    return r;
  } else
  return 0;
}
long getPos2(boolean status){
  
int newPosition = myEnc2.read();
  if (newPosition != oldPosition2) {
     long r;
    if(status)
       r = map((newPosition - oldPosition2), init2min, init2max, 2000, 4000);
    else
      r = newPosition - oldPosition2;
    oldPosition2 = newPosition;
    return r;
  } else
  return 0;
}
long getPos3(boolean status){
  
long newPosition = myEnc3.read();
  if (newPosition != oldPosition3) {
     long r;
    if(status)
       r = map((newPosition - oldPosition3),init3max, init3min,  2000, 4000);
    else
      r = newPosition - oldPosition3;
    oldPosition3 = newPosition;
    return r;
  } else
  return 0;
}
long getPos4(boolean status){
  
long newPosition = myEnc4.read();
  if (newPosition != oldPosition4) {
     long r;
    if(status)
       r = map((newPosition - oldPosition4), init4min, init4max, 2000, 4000);
    else
      r = newPosition - oldPosition4;
    oldPosition4 = newPosition;
    return r;
  } else
  return 0;
}
