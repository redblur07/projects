/*
 * First C++ program that says hello (hello.cpp)
 */
#include <iostream>    // Needed to perform IO operations
using namespace std;

int number1, number2, sum;
 
int main() {                        // Program entry point
   cout << "Enter a number" << endl;  // Say Hello
   cin >> number1;
   
   cout << "Enter second number" << endl;  // Say Hello
   cin >> number2;
   
   sum = number1 + number2;
   
   cout << "Sum of your numbers is " << sum << endl;
   
   return 0;                        // Terminate main()
} 
