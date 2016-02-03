package com.example.tutorial;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

import com.example.tutorial.AddressBookProtos.AddressBook;
import com.example.tutorial.AddressBookProtos.Person;

public class AddPerson {
	static{
		System.loadLibrary("imp");
	}
	
	native static void print_addressbook_in_cpp(AddressBook addressbook);
	
	static Person PromptForAddress(
			BufferedReader stdin,
			PrintStream stdout) throws NumberFormatException, IOException{
		Person.Builder person = Person.newBuilder();
		
		stdout.print("Enter Person ID:");
		person.setId(Integer.valueOf(stdin.readLine()));
		
		stdout.print("Enter Name:");
		person.setName(stdin.readLine());
		
		stdout.print("Enter Email:");
		String email = stdin.readLine();
		if ( email.length() > 0 ) person.setEmail(email);
		
		return person.build();
	}
	
	public static void main(String[] args){
		if ( args.length != 1 ) {
			System.err.println("Incorrect usage");
			System.exit(-1);
		}
		
		AddressBook.Builder addressBook = AddressBook.newBuilder();
		
		try {
			FileInputStream input = new FileInputStream(args[0]);
			if ( input != null  ) addressBook.mergeFrom(new FileInputStream(args[0]));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			addressBook.addPerson(
						PromptForAddress(new BufferedReader(new InputStreamReader(System.in)),
								System.out)
						);
		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			addressBook.build().writeTo(new FileOutputStream(args[0]));
			print_addressbook_in_cpp(addressBook.build());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
