
from z3 import *
import javaobj
import numpy as np
import os
import time
import sys

def read_file(self, filename, stream=False):
        """
        Reads the content of the given file in binary mode
        :param filename: Name of the file to read
        :param stream: If True, return the file stream
        :return: File content or stream
        """
        for subfolder in ('java', ''):
            found_file = os.path.join(
                os.path.dirname(__file__), subfolder, filename)
            if os.path.exists(found_file):
                break
        else:
            raise IOError("File not found: {0}".format(filename))

        if stream:
            return open(found_file, "rb")
        else:
            with open(found_file, 'rb') as filep:
                return filep.read()

numbOfStrings=10
fileN = "6_DFA_theone.ser"
Display = True
num = None

# Run script directly use default dfa and enter params
if(len(sys.argv)>1):
    fileN = str(sys.argv[1]) 
    num = int(sys.argv[2])
    numbOfStrings = int(sys.argv[3])
    Display = False

# Convert java objects to python objects
jobj = read_file('o','Dfa/'+str(fileN))
ajobj = read_file('o','Dfa/'+str(fileN[0])+'_final_States.ser')
bjobj = read_file('o','Dfa/'+str(fileN[0])+'_Alphabet.ser')
mat = javaobj.loads(jobj)
fina = javaobj.loads(ajobj)
alpha = javaobj.loads(bjobj)
folder = os.listdir("Dfa")

# Make sure intergers are accepted as input
def inp(s=''):
    try:
        numb = int(input(s))
    except ValueError:
        print("you must enter an integer")
        numb = inp()
    return numb

# Fill compliment table
l = len(mat)
lrow = len(mat[0])
compliment = np.zeros((l,l,lrow))
for i in range(l):
    for j in range(lrow):
        for k in range(l):
            if(mat[i][j]==k):
                compliment[i][k][j]=1

# Display Dfa if not accesed through Cyc.jar
if Display:
    print("\nDfa: ",fileN)
    print(mat)
    print("Final states: ",fina)
    print("Alphabet: ",alpha,"\n")
#print(compliment)

# Get user input for string sequence length
if num is None:
    num = inp("Enter String Length: ")
start_time = time.clock()

# Set up sequence array
T = [ Int('s_%s' % i) for i in range(num+1) ]
W = [ Int('j_%s' % i) for i in range(num) ]

c1 = True
s = Solver()

# Constrain to existing states
for i in range(len(T)):
    c1=And(c1,And(T[i]>=0,T[i]<len(mat)))
for i in range(len(W)):
    c1=And(c1,And(W[i]>=0,W[i]<len(alpha)))

# Coonstrain states (Not (And (Ti = state, Ti+1 = disallowed state given Ti,Wi)))
for i in range(len(T)-1):
    for j in range(l):
        for k in range(l):
            for li in range(len(alpha)):
                if(1 != compliment[j][k][li]):
                    c1 = And(c1, Not(And(T[i]==j,T[i+1]==k,W[i]==li)));

# Final and start state constraints
finCon = [ Bool('c_%s' % i) for i in range(len(fina)) ]
for i in range(len(finCon)):
    finCon[i] = T[num]==fina[i]
c1 = And(c1 , Or( finCon ))
c1 = And(c1 , T[0]==0)

# Put through solver
s.add(c1)
print("\n"+str(s.check()))
v = Solver()
po = 0
ansar=[ "" for i in range(numbOfStrings+1)]

while s.check() == sat and po<numbOfStrings:
    po += 1
    m = s.model()
    print("\n"+str(po))

    # Get next sequence
    Con = [ Bool('ci_%s' % i) for i in range(len(T)+len(W)) ]
    for i in range(len(T)):
        Con[i] = T[i] != m[T[i]]
    for i in range(len(T),len(T)*2-1):
        Con[i] = W[i-len(T)] != m[W[i-len(T)]]
    s.add(Or(Con))

    # Construct String
    ans = "-> "
    for i in range(len(W)):
        ans += alpha[int(str(m[W[i]]))]
    print (ans)
print ("\n--> ",time.clock() - start_time, "seconds\n")

# Incase number of strings generated is less than number of strings asked for
if po != numbOfStrings and po != 0:
    print("\nNo more strings available")
