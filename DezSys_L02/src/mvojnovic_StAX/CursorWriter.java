/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package mvojnovic_StAX;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;

import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;

import javax.xml.namespace.QName;

/**
 * CursorWriter sample is used to demonstrate the use of STAX Writer api's.
 *
 * @author k.venugopal@sun.com
 */

public class CursorWriter {
    
    private static String filename = null;
    
    private static void printUsage() {
        System.out.println("usage: java -Djava.endorsed.dirs=<jaxp dist/lib directory> stax.writer.CursorWriter -f <outputFileName>");
    }
    
    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        try{
            String fileName = null;
            try{
                if(args[0].equals("-f")){
                    fileName = args[1];
                }
                else{
                    printUsage() ;
                    return;
                }
            }catch(Exception ex){
                printUsage() ;
                return;
            }
            

            XMLInputFactory xif = XMLInputFactory.newInstance();
            XMLEventReader xtr = xif.createXMLEventReader(new FileInputStream(fileName),"utf-8");
            
            
            XMLOutputFactory xof =  XMLOutputFactory.newInstance();
            XMLStreamWriter xtw = null;
            
            xtw = xof.createXMLStreamWriter(new FileOutputStream(fileName),"utf-8"); 
            
            while(xtr.hasNext()){
            	XMLEvent event = xtr.nextEvent();
            	switch (event.getEventType()){
            	case XMLEvent.START_DOCUMENT:
            		
            		xtw.writeStartDocument("utf-8","1.0");
            		xtw.writeCharacters("\n");
                    
            		break;
                case XMLEvent.START_ELEMENT:
                	if(event.asStartElement().getName().getLocalPart() == "ph"){
                		String elm = askForElement();
                		if(elm.equals("false")){
                			xtw.writeStartElement("ph");
                    		xtw.writeEndElement();
                    		xtw.writeCharacters("\n");
                		}else{
                		String att[] = askForAttribute();
                		String value = askForValue();
                		xtw.writeStartElement(elm);
                		xtw.writeAttribute(att[0], att[1]);
                		xtw.writeCharacters(value);
                		xtw.writeEndElement();
                		xtw.writeCharacters("\n");
                		xtw.writeStartElement("ph");
                		xtw.writeEndElement();
                		xtw.writeCharacters("\n");
                		}
                		
                	}else{
	                    xtw.writeStartElement(event.asStartElement().getName().getLocalPart());
	                    Iterator<Attribute> i = event.asStartElement().getAttributes();
	                    while(i.hasNext()){
	                    	Attribute attribute = i.next();
	                    	xtw.writeAttribute(attribute.getName().toString(),attribute.getValue());
	                    }
                	}
                    break;
                case XMLEvent.CHARACTERS:
                	xtw.writeCharacters(event.asCharacters().getData());
                    break;
                case XMLEvent.END_ELEMENT:                	
                	xtw.writeEndElement();
                	break;
            	case XMLEvent.END_DOCUMENT:
                    break;
            	}
            	
            	
            		
        		
            	
            }
            	
            xtw.writeCharacters("\n");
            xtw.writeStartElement("Schulinformation");
            xtw.writeAttribute("name", "TGM");
            xtw.writeStartElement("Standort");
            xtw.writeCharacters("Wien");
            xtw.writeEndElement();
            xtw.writeEndElement();
            xtw.writeEndDocument();
//            xtw.writeComment("all elements here are explicitly in the HTML namespace");
//            xtw.writeStartDocument("utf-8","1.0");
//            xtw.setPrefix("html", "http://www.w3.org/TR/REC-html40");
//            xtw.writeStartElement("http://www.w3.org/TR/REC-html40","html");
//            xtw.writeNamespace("html", "http://www.w3.org/TR/REC-html40");
//            xtw.writeStartElement("http://www.w3.org/TR/REC-html40","head");
//            xtw.writeStartElement("http://www.w3.org/TR/REC-html40","title");
//            xtw.writeCharacters("Frobnostication");
//            xtw.writeEndElement();
//            xtw.writeEndElement();
//            xtw.writeStartElement("http://www.w3.org/TR/REC-html40","body");
//            xtw.writeStartElement("http://www.w3.org/TR/REC-html40","p");
//            xtw.writeCharacters("Moved to");
//            xtw.writeStartElement("http://www.w3.org/TR/REC-html40","a");
//            xtw.writeAttribute("href","http://frob.com");
//            xtw.writeCharacters("here");
//            xtw.writeEndElement();
//            xtw.writeEndElement();
//            xtw.writeEndElement();
//            xtw.writeEndElement();
//            xtw.writeEndDocument();
//            xtw.flush();
//            xtw.close();
        }catch(Exception ex){
            ex.printStackTrace();
            System.err.println("Exception occurred while running writer samples");
        }
        System.out.println("Done");
    }

    public static String askForElement(){
    	System.out.println("Name des Elementes(false wenn sie nichts einfuegen wollen)");
    	String elm = new Scanner(System.in).nextLine();
    	return elm;
    }
    public static String[] askForAttribute(){
    	System.out.println("Name des Attributes");
    	String att = new Scanner(System.in).nextLine();
    	System.out.println("Welche Value im Atribute");
    	String value_att = new Scanner(System.in).nextLine();
    	String[] help = new String[2];
    	help[0] = att;
    	help[1] = value_att;
    	return help;
    }
    public static String askForValue(){
    	System.out.println("Value im Tag");
    	String value = new Scanner(System.in).nextLine();
    	return value;
    }
}