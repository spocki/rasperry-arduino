void setup(){
  Serial.begin(115200);
}

int i=0;
int const commandBufferSize = 100;
char commandbuffer[commandBufferSize];

void in(char* in){
  // char c = in[0];
  Serial.print(">BACK:");
  Serial.print(in);
  Serial.print("<");
}

void loop(){
  if(Serial.available()){
     while( Serial.available()) {
        int b = Serial.read();
        if (b == '>')  {
          i = 0;
        } 
        else if (b == '<')  {
          if (i > 0) {
            in((char*)commandbuffer);
          }
          // CLEAR BUFFER
          for (int c = 0 ; c < commandBufferSize ; c++) {
            commandbuffer[c]='\0';
          }
          i = 0;
        } else {
          commandbuffer[i++] = b; 
        }
     }
  }
}

