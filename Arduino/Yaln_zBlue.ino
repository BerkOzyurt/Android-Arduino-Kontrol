#include <LiquidCrystal.h>

LiquidCrystal lcd(8, 9, 4, 5, 6, 7);


//Ses Sensörü Ayarları
int ses_sensoru = 34;
int role = 35;
int alkis = 0;
long algilama_araligi_baslangic = 0;
long algilama_araligi = 0;
boolean isik_durumu = false;

//Işık Sensörü Ayarları
int ledler[] = {22, 24, 26, 28};
int led_sayisi = 4;
int ldr_pin = A15;
int deger = 0;

//Sıcaklık Sensörü Ayarları
float sicaklik; //Analog değeri dönüştüreceğimiz sıcaklık 
float analoggerilim; //Ölçeceğimiz analog değer

char data = 0;            //Bluetooth'tan gelen veri

void setup()
{

  lcd.begin(16, 2);
  // Birinci satıra "Oda Sıcaklığı:" yaz.
  lcd.print("Oda sicakligi:");
  
   //Ses için giriş ve çıkışlar
  pinMode(ses_sensoru, INPUT);
  pinMode(role, OUTPUT);
  
  //Işık için giriş ve çıkışlar
  for (int i = 0 ; i < led_sayisi ; i++)
  {
    pinMode(ledler[i], OUTPUT);
  }

  //Genel gereklilik
  Serial.begin(9600);
                                   
  pinMode(13, OUTPUT);
  pinMode(12, OUTPUT);
}
void loop()
{ 
  //isik();
  //ses();
  sicaklikC();
   if(Serial.available() > 0)      
   {
      data = Serial.read();       
      Serial.print(data);          
      Serial.print("\n");        
      if(data == '1')              
         digitalWrite(13, HIGH);
         digitalWrite(12, LOW);
         ses();   
      else if(data == '0')         
         digitalWrite(13, LOW);
         digitalWrite(12,HIGH);
         isik();    
         
   }
   
   
}

void ses(){
  
  int sensor_durumu = digitalRead(ses_sensoru);                   //Ses sensörünü dinler ve sensor_durumu değişkenine kaydeder.
  if (sensor_durumu == 0){                                        //Eğer durum 0 ise
    if (alkis == 0){                                              //Eğer alkışlanmamışsa
      algilama_araligi_baslangic = algilama_araligi = millis();   //Alkışı algılama aralığı ledin yanmasına kadar geçen süreye eşitlenir.
      alkis++;                                                    //Alkış değeri 1 arttırılır.
    }
    else if (alkis > 0 && millis()-algilama_araligi >= 50){       //Eğer alkış 0 ise ve aralık 50'den büyük ise
      algilama_araligi = millis();                                //Algılanma aralığını direk programın süresi yapar.
      alkis++;                                                    //Alkış değeri 1 arttırılır.
    }           
  }
  if (millis()-algilama_araligi_baslangic >= 400){                //Eğer alkış aralığı 400'den büyük ise
    if (alkis == 2){                                              //Ve alkış değeri 2 ise
      if (!isik_durumu){                                          //Işık durumu true değil ise
        isik_durumu = true;                                       //Işık durumunu true yapar
        digitalWrite(role, HIGH);                                 //Ve ışığı yakar.
        Serial.print("Işık Yandı \n");                            //Işığın yandığını monitöre yazdırır.
      }
      else if (isik_durumu){                                      //Işık durumu true ise
        isik_durumu = false;                                      //false yapar    
        digitalWrite(role, LOW);                                  //Işığı söndürür
        Serial.print("Işık Söndü \n");                            //Ve söndüğünü ekrana yazar
      }
    }
    alkis = 0;                                                    //Alkış değerini tekrar 0 yaparak başa döner.
  } 
}

void isik(){
  deger = analogRead(ldr_pin);    //LDR pinindeki değeri okur.
  if (deger > 0 && deger <= 255)    //Değer bu aralıktaysa yalnız 1. led yanar.
  {
    digitalWrite(ledler[0], HIGH);
    digitalWrite(ledler[1], LOW);
    digitalWrite(ledler[2], LOW);
    digitalWrite(ledler[3], LOW);
    Serial.print ("1. Led");        //Hangi ledin yandığını yazar
    Serial.print ("\n");
    
  }
  if (deger > 256 && deger <= 511)  //Değer bu aralıktaysa yalnız 2 led yanar.
  {
    digitalWrite(ledler[0], HIGH);
    digitalWrite(ledler[1], HIGH);
    digitalWrite(ledler[2], LOW);
    digitalWrite(ledler[3], LOW);
    Serial.print ("2. Led");        //Hangi ledin yandığını yazar
    Serial.print ("\n");
    
  }
  if (deger > 512 && deger <= 767)  //Değer bu aralıktaysa yalnız 3 led yanar.
  {
    digitalWrite(ledler[0], HIGH);
    digitalWrite(ledler[1], HIGH);
    digitalWrite(ledler[2], HIGH);
    digitalWrite(ledler[3], LOW);
    Serial.print ("3. Led");        //Hangi ledin yandığını yazar
    Serial.print ("\n");
    
  }
  if (deger > 768 && deger <= 1023) //Değer bu aralıktaysa yalnız 4 led yanar.
  {
    digitalWrite(ledler[0], HIGH);
    digitalWrite(ledler[1], HIGH);
    digitalWrite(ledler[2], HIGH);
    digitalWrite(ledler[3], HIGH);
    Serial.print ("4. Led");        //Hangi ledin yandığını yazar
    Serial.print ("\n");
    
  }
  
}

void sicaklikC(){
  analoggerilim = analogRead(A14);             //A2'den değeri ölç
  analoggerilim = (analoggerilim/1023)*5000;  //değeri mV'a dönüştr 
  sicaklik = analoggerilim /10,0;             // mV'u sicakliğa dönüştür

  
  lcd.setCursor(0, 1);
  lcd.print(sicaklik);     // hesaplanan sıcaklığı yazdır. 
  lcd.print(" derece");    // devamına birimi olan derecece yaz.
  delay (1000);
  
  
}


