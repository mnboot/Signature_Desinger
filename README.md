# Signature Desinger
RSA generator that generate public key, with corresponding private key, with a digital signature 
that way you can verify whether a sender is legit or not <br/>

## How does it work
A normal Digital Signature looks as follows:
![digital signature sequence diagram](img/Digital%20Signature.svg)
while this seems reliable, it has an issue: how do you know whether the public key is from **him**.<br/>
### Issue
let us suppose the Receiver does not know what Sender public key, this what could happen
![Issue with Digital Signature](img/Issue%20with%20Digital%20Signature.svg)
This may happen because everyone can effortlessly generate public/private key. 
If only there is a way to make generating keys hard and trustworthy.
### The Solution

The solution is to make generating a key require to satisfy a hard requirements. <br/>
That is, a key must have a long name that identify who is the owner 
AND it also must be hard to generate that identifier.

![Solution vs Fake Sender](img/Solution%20Fake%20Sender.svg)

#### How
1. chose a phrase e.g. "RlSndr"
2. Generate RSA PEM(Base64) public/private key files
3. Get the base64 text inside the public key file
4. Hash it - so that it generate gibberish Hex digits
5. Base64 encode - so that it generate random letters and numbers
6. check if the base64 encoded text(step 5) start with the phrase(step 1)
7. repeat step 2 to 6 until they match

---

# Signature Desinger Tool
I built a GUI tool that does all hard working for you from generating RSA with your phrase to verify whether a key match a pharse


And yes it spelled _desinger_ not designer. I misspelled it and realized it late, 
in addition, I couldn't bother fixing the name because I believe it may break my code, IDE, and repo. <br>
So I officially called it "Signature Desinger"