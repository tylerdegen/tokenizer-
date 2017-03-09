
//package edu.c3341;

/**
 * Token kinds needed for Part 1 of the Core Interpreter project.
 *
 * @author Wayne D, Heym
 *
 */
enum TokenKind {

    /**
     * Test driver's token number = 1, token is program
     */
    PROGRAM(1),
    
        /**
     * Test driver's token number = 1, token is program
     */
    BEGIN(2),
    
    /**
     * Test driver's token number = 1, token is program
     */
    END(3),
    
    /**
     * Test driver's token number = 1, token is program
     */
    INT(4),
    
    /**
     * Test driver's token number = 1, token is program
     */
    IF(5),
    
    /**
     * Test driver's token number = 1, token is program
     */
    THEN(6),
    
    /**
     * Test driver's token number = 1, token is program
     */
    ELSE(7),
    
    /**
     * Test driver's token number = 1, token is program
     */
    WHILE(8),
    
    /**
     * Test driver's token number = 1, token is program
     */
    LOOP(9),
    
    /**
     * Test driver's token number = 1, token is program
     */
    READ(10),
    
    /**
     * Test driver's token number = 1, token is program
     */
    WRITE(11),
    
    /**
    * 
    */

        /**
         * Test driver's token number = 12; token is ;.
         */
        SEMICOLON(12),
		
		        /**
         * Test driver's token number = 12; token is ,.
         */
        COMMA(13),

        /**
         * Test driver's token number = 14; token is =.
         */
        ASSIGNMENT_OPERATOR(14),
		
		        /**
         * Test driver's token number = 14; token is =.
         */
        EXCLAMATION(15),
		
		        /**
         * Test driver's token number = 14; token is =.
         */
        LEFT_BRACKET(16),
		
		        /**
         * Test driver's token number = 14; token is =.
         */
        RIGHT_BRACKET(17),
		
		        /**
         * Test driver's token number = 14; token is =.
         */
        AND_OPERATOR(18),

        /**
         * Test driver's token number = 19; token is ||.
         */
        OR_OPERATOR(19),
		
		        /**
         * Test driver's token number = 14; token is =.
         */
        LEFT_PAR(20),
		
		        /**
         * Test driver's token number = 14; token is =.
         */
        RIGHT_PAR(21),
		
		        /**
         * Test driver's token number = 14; token is =.
         */
        PLUS(22),
		
		        /**
         * Test driver's token number = 14; token is =.
         */
        MINUS(23),
		
		        /**
         * Test driver's token number = 14; token is =.
         */
        MULT(24),
		
		        /**
         * Test driver's token number = 14; token is =.
         */
        NOT_EQUAL(25),

        /**
         * Test driver's token number = 26; token is ==.
         */
        EQUALITY_TEST(26),
		
		        /**
         * Test driver's token number = 14; token is =.
         */
        LESS(27),
		
		        /**
         * Test driver's token number = 14; token is =.
         */
        GREATER(28),
		
		        /**
         * Test driver's token number = 14; token is =.
         */
        LESS_EQUAL(29),
		
		        /**
         * Test driver's token number = 14; token is =.
         */
        GREATER_EQUAL(30),

        /**
         * Test driver's token number = 31.
         */
        INTEGER_CONSTANT(31),

        /**
         * Test driver's token number = 32.
         */
        IDENTIFIER(32),

        /**
         * Test driver's token number = 33.
         */
        EOF(33),

        /**
         * Test driver's token number = 34.
         */
        ERROR(34);

    /**
     * Test driver's token number.
     */
    private int testDriverTokenNumber;

    /**
     * Constructor.
     *
     * @param number
     *            the test driver's token number
     */
    private TokenKind(int number) {
        this.testDriverTokenNumber = number;
    }

    /**
     * Return test driver's token number.
     *
     * @return test driver's token number
     */
    public int testDriverTokenNumber() {
        return this.testDriverTokenNumber;
    }
}
