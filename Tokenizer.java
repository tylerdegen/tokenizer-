//package edu.c3341;
import java.io.*;
import java.util.Scanner;

/**
 * Tokenizer for Core Interpreter project. (Note: by package-wide convention,
 * unless stated otherwise, all references are non-null.)
 *
 * @author Wayne D. Heym
 *
 * @mathsubtypes <pre>
 * TOKENIZER_MODEL is (
 *   front: string of character,
 *   remainder: string of character)
 *  exemplar tzr
 *  constraint
 *   [tzr.front cannot be extended by the first character of tzr.remainder
 *    to be the prefix of any legal token kind]
 * </pre>
 * @mathdefinitions <pre>
 * AGGREGATE(
 *   ss: string of string of character
 *  ): string of character satisfies
 *  if ss = <> then
 *    AGGREGATE(ss) = <>
 *  else
 *    AGGREGATE(ss) = ss[0,1) * AGGREGATE(ss[1,|ss|))
 * </pre>
 * @mathmodel type Tokenizer is modeled by TOKENIZER_MODEL
 *
 */
public class Tokenizer {

    /**
     * Java interfaces cannot contain constructors pertaining to the classes
     * that are to implement them; hence, here in this Javadoc comment
     * expectations for constructors are stated. Each class that implements
     * interface Tokenizer is expected to follow the singleton pattern; hence,
     * each such class should have exactly one private constructor. To be a bit
     * more general about it, every constructor for each such class must be
     * private.
     *
     * In Java versions 7 and below, interfaces cannot contain static methods.
     * To remain compatible with these Java versions, this interface presents
     * the expected static methods here in a Javadoc comment.
     *
     * /** If no instance of Tokenizer yet exists, create one; in any case,
     * return a reference to the single instance of the Tokenizer.
     *
     * @param itString
     *            the Iterator<String> from which tokens will be extracted;
     *            Tokenizer expects itString's next() method never to deliver an
     *            empty String or a String containing whitespace.
     * @return the single instance of the Tokenizer
     * @updates itString
     * @ensures <pre>[the reference create returned is the reference to the
     *                newly created and only instance of the class implementing
     *                the Tokenizer interface]  and
     *          there exists content: string of character
     *            (content = AGGREGATE(~#itString.unseen)  and
     *             create.front * create.remainder = content  and
     *             (([no prefix of content is a legal token] and
     *               [create.front is the text of an ERROR token]) or
     *              ([create.front is the text of a legal token kind] and
     *               [create.front cannot be extended by the first character
     *                of create.remainder to be the prefix of any
     *                legal token kind])))
     *       </pre> public static Tokenizer create(Iterator<String> itString)
     *
     */

    /**
     * /** Return either null or the single instance of the Tokenizer, if it
     * exists.
     *
     * @return either null or the single instance of the Tokenizer, if it exists
     *
     *         public static Tokenizer instance()
     *
     */

    public Scanner scan;
    //    public Iterator<String> itString;
    public String currentLine;
    public int index = 0;
    public char Tok = 'a';

    public Tokenizer(Scanner in){
        scan = in;
		if (scan.hasNext()){
        currentLine = scan.nextLine();
		} else{
			currentLine = "";
		}
    }

    /**
     * Return the kind of the front token. (Restores this.)
     *
     * @return the kind of the front token
     * @ensures getToken = [the kind of token this.front]
     */
    TokenKind getToken(){

		//end of file, check if index is length of string because -1 will be last char
		//need to also check if scan has next?
		//skip token sets it as MINVALUE if eof
        if (currentLine.length() == 0 || index == currentLine.length() - 1 || Tok == Character.MIN_VALUE){
                return TokenKind.EOF;
        }
        TokenKind getTok = TokenKind.ERROR;
		//get the token
        Tok = currentLine.charAt(index);
		
		//currently looking at a lowercase
        if (Character.isLowerCase(Tok)){
            getTok = TokenKind.LOWER_CASE_WORD;
        }
		
		
		//semicolon
        else if (Tok == ';'){
            getTok = TokenKind.SEMICOLON;
        }
		/*
		//equality test or assignment?
        else if (Tok == '='){
            if (currentLine.charAt(index + 1) == '='){
                getTok = TokenKind.EQUALITY_TEST;
            }
            else{
                getTok = TokenKind.ASSIGNMENT_OPERATOR;
            }
        }
		
		//or operator or error?
        else if (Tok == '|'){
            if (currentLine.charAt(index + 1) =='|'){
                getTok = TokenKind.OR_OPERATOR;
            }
            else{
                getTok = TokenKind.ERROR;
            }
        }
		
		//Integer
        else if (Character.isDigit(Tok)){
            getTok = TokenKind.INTEGER_CONSTANT;
        }
		
		//Uppercase Identifier
        else if (Character.isUpperCase(Tok)){
            getTok = TokenKind.IDENTIFIER;
        }
		
		*/
        return getTok;
    };

    /**
     * Skip front token.
     *
     * @updates this
     * @ensures <pre>(if [the token kind of #this.front is good and legal]
     *                  then this.front * this.remainder = #this.remainder)  or
     *          ([the token kind of #this.front is EOF] and
     *          this = #this)</pre>
     */
    void skipToken(){
        char Tok = currentLine.charAt(index);
                //get next line if it exists
        if (index + 1 == currentLine.length()){
            if (scan.hasNext()){
                currentLine = scan.nextLine();
            }
            else{
                //somehow set Tok as end of file
                Tok = Character.MIN_VALUE;
                return;
            }
        }
        //a lot of these could be lumped, but are separated for the sake of being explicit
        if (Character.isLowerCase(Tok)){
            while (Character.isLowerCase(Tok) && index + 1 < currentLine.length()){
                Tok = currentLine.charAt(index + 1);
                index += 1;
            }
        }
		
		
        else if (Tok == ';'){

            Tok = currentLine.charAt(index + 1);
            index += 1;
        }
		/*
        else if (Tok == '='){
            if (currentLine.charAt(index + 1) == '='){
                //skip two
                //getTok = TokenKind.EQUALITY_TEST;
                Tok = currentLine.charAt(index + 2);
                index += 2;
            }
            else{
                //getTok = TokenKind.ASSIGNMENT_OPERATOR;
                Tok = currentLine.charAt(index + 1);
                index += 1;
            }
        }
        else if (Tok == '|'){
            if (currentLine.charAt(index + 1) =='|'){
                Tok = currentLine.charAt(index + 2);
                index += 2;
                //getTok = TokenKind.OR_OPERATOR;
            }
            else{
                Tok = currentLine.charAt(index + 1);
                index += 1;
                //getTok = TokenKind.ERROR;
            }
        }
        else if (Character.isDigit(Tok)){
            while (Character.isDigit(Tok)){
                Tok = currentLine.charAt(index + 1);
                index += 1;
            }
            //getTok = TokenKind.INTEGER_CONSTANT;
        }
        else if (Character.isUpperCase(Tok)){
            //getTok = TokenKind.IDENTIFIER;
            while (Character.isUpperCase(Tok)){
                Tok = currentLine.charAt(index + 1);
                index += 1;
            }
        }
*/
        //cut through white space
        while (Tok == ' '){
                index += 1;
                Tok = currentLine.charAt(index + 1);
        }




    };

    /*
     * For Part 1 of the Core Interpreter project, the following two methods
     * need not be implemented.
     */

    /**
     * Return the integer value of the front INTEGER_CONSTANT token. (Restores
     * this.)
     *
     * @return the integer value of the front INTEGER_CONSTANT token
     * @requires [the kind of this.front is INTEGER_CONSTANT]
     * @ensures intVal = [the integer value of this.front]
     */
    int intVal(){
        return 0;
    };

    /**
     * Return the name of the front IDENTIFIER token. (Restores this.)
     *
     * @return the name of the front IDENTIFIER token
     * @requires [the kind of this.front is IDENTIFIER]
     * @ensures intVal = this.front
     */
    String idName(){
        return "okay";
    };
}
