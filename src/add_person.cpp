#include <iostream>
#include <fstream>
#include <string>

#include "addressbook.pb.h"

using namespace std;

void PromptForAddress(
		tutorial::Person* person){
	cout << "Enter User ID:";
	int id;
	cin >> id;
	person->set_id(id);
	cin.ignore(256, '\n');

	cout << "Enter Name:";
	getline(cin, *person->mutable_name());

	cout<< "Enter Email:";
	string email;
	getline(cin, email);
	if ( !email.empty() )person->set_email(email);
}

int main(int argc, char* argv[]){
	GOOGLE_PROTOBUF_VERIFY_VERSION;

	if (argc != 2){
		cout<< "Incorrect usage." <<endl;
		return -1;
	}

	tutorial::AddressBook address_book;
	{
		fstream input(argv[1], ios::in | ios::binary);
		if ( !input ) cout << "File does not exist.";
		else if ( !address_book.ParseFromIstream(&input) ){
			cerr << "Fail to parse address book";
		}
	}

	PromptForAddress(address_book.add_person());

	{
		fstream output(argv[1], ios::out | ios::trunc | ios::binary);
		if ( !address_book.SerializeToOstream(&output) ){
			cerr << " Failed to write address book " <<endl;
			return -1;
		}
	}


	google::protobuf::ShutdownProtobufLibrary();
	return 0;
}
