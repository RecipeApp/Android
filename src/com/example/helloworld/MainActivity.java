package com.example.helloworld;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Spinner;

public class MainActivity extends Activity {

Spinner spinner;
List<String> ingredient = new ArrayList<String>();
List<String> effect1 = new ArrayList<String>();
List<String> effect2 = new ArrayList<String>();
List<String> effect3 = new ArrayList<String>();
List<String> effect4 = new ArrayList<String>();

XmlResourceParser myXml;
int eventType;
String nodeValue;

@Override
public void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    loadXml(); // Reads XML and loads it into Lists, One list for each element
    convertListToSpinner(); // Takes Lists and merges data with Spinners (Currently Unimplemented)


}


private void convertListToSpinner() {
    // TODO Auto-generated method stub

}


private void loadXml() {
    // TODO Auto-generated method stub
	System.out.println("Inside LoadXml");
    myXml = getBaseContext().getResources().getXml(R.xml.ingredients);
    try {
        myXml.next();

    eventType = myXml.getEventType(); // Get current xml Event

    } catch (XmlPullParserException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }

    while(eventType != XmlPullParser.END_DOCUMENT){
    	System.out.println("Inside While Loop Step 1");
        if(eventType == XmlPullParser.START_DOCUMENT){
            // checks can be placed here to make sure file is read
        }
        else if(eventType == XmlPullParser.START_TAG){
            nodeValue = myXml.getName();

            try{
            	System.out.println("Inside While Loop Step 2");
            if(nodeValue.equalsIgnoreCase("Ingredients")){ //Finds Ingredient tag
            	System.out.println("Inside While Loop Step 3");
            myXml.next(); //Since the ingredient tag does not hold the text, we go to next element area
            ingredient.add(myXml.getText().trim()); // Set the text of the Ingredient tag to list
            }

            //Since the effect tags are followed by ending tags, we can just do nextText() to get tag text
            if(nodeValue.equalsIgnoreCase("Effect1")){
                effect1.add(myXml.nextText().trim()); // Set the text of the Effect1 tag to list
            }
            if(nodeValue.equalsIgnoreCase("Effect2")){
                effect2.add(myXml.nextText().trim()); // ditto
            }
            if(nodeValue.equalsIgnoreCase("Effect3")){
                effect3.add(myXml.nextText().trim()); // ditto
            }
            if(nodeValue.equalsIgnoreCase("Effect4")){
                effect4.add(myXml.nextText().trim()); // ditto
            }
            }catch(Exception e){
                e.getMessage();
            }


        }
        try {
            eventType = myXml.next();
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    for(int i = 0; ingredient.size() > i; i++){
        System.out.println(ingredient.get(i));

    }

}


@Override
public boolean onCreateOptionsMenu(Menu menu) {
  //  getMenuInflater().inflate(R.menu.activity_main, menu);
    return true;
}




}
