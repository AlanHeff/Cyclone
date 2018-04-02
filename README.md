# Cyclone
Dfa saver/tester

(Works on mac, and linux)
- Download all files
- Unzip Dfa.zip
- Unzip z3.zip
- run with:
<div><pre>
javac DFA.java
java DFA
</pre></div>

To generate strings, load a saved DFA (Enter 'L' in main menu then enter DFA file number).
at Generate strings type 'Y' then enter length of string to be generated and number of strings.
it will tell you the shortest string acceptable by the DFA by typing 'S' at "Generate String".

You can then test the strings generated by Typing 'Y' at "test strings?" and copy and pasting them
in or write in your own strings. exit test mode with '_b'

Can create new DFAs also (up to 9) 'C' in main menu and follow instructions and delete 'D' 
then 'Q' to end program

main menu:
<div><pre>
Choose option:<br>

  Load DFA: L
  Create DFA: C
  Delete DFA: D
  Quit: Q
</pre></div>
pre saved DFA:
<div><pre>
  File 1_DFA_Even_as.ser                       {a,b} Any even number of a's.
  File 2_DFA_three_as.ser                      {a,b} At least 3 a's.
  File 3_DFA_BinaryGreaterThanFive.ser         {1,0} Binary numbers > 5.
  File 4_DFA_FiftyCentCoke.ser                 {t,w,f} Ten,twenty,fifty, make up at least 50 cent.
  File 5_DFA_OneThirdPositionFromEnd.ser       {0,1} One in third position from end.
  File 6_DFA_theone.ser                        {a,b} Contains substring abb.
  File 7_DFA_ThirtyCent_QuaterNickleDime.ser.  {q,n,d} quater, nickle, dime, make at least up 35c.
  File 8_DFA_AnyOneToFive.ser                  {1,2,3,4,5} Any string.
  File 9_DFA_onlyA.ser                         {a} a, nothing else.
</pre></div>
