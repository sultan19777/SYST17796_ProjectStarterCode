/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ca.sheridancollege.project;

/**
 *
 * @author win 11
 */
public class BlackjackCard extends Card {

    private final Rank rank;
    private final Suit suit;

    /**
     * Constructor for a BlackjackCard.
     * @param rank The rank of the card.
     * @param suit The suit of the card.
     */
    public BlackjackCard(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public Rank getRank() {
        return rank;
    }

    public Suit getSuit() {
        return suit;
    }

    /**
     * Returns the standard Blackjack value of the card.
     * Aces return 11 by default.
     * Face cards (Jack, Queen, King) return 10.
     * Number cards return their face value.
     * @return The numerical value of the card.
     */
    public int getValue() {
        switch (rank) {
            case ACE:
                return 11;
            case JACK:
            case QUEEN:
            case KING:
                return 10;
            default:
                // For ranks TWO through TEN, the ordinal + 1 gives the correct value.
                // e.g., TWO is ordinal 1, so value is 2. TEN is ordinal 9, value is 10.
                return rank.ordinal() + 1;
        }
    }

    /**
     * Checks if the card is an Ace.
     * @return true if the card is an Ace, false otherwise.
     */
    public boolean isAce() {
        return rank == Rank.ACE;
    }

    /**
     * Returns a string representation of the card.
     * @return A string in the format "Rank of Suit", e.g., "Ace of Spades".
     */
    @Override
    public String toString() {
        // Capitalize the first letter and lowercase the rest for prettier output
        String rankStr = rank.toString().charAt(0) + rank.toString().substring(1).toLowerCase();
        String suitStr = suit.toString().charAt(0) + suit.toString().substring(1).toLowerCase();
        return rankStr + " of " + suitStr;
    }
}
