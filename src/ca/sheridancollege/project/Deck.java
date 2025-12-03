/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ca.sheridancollege.project;

/**
 *
 * @author win 11
 */
public class Deck extends GroupOfCards {

    /**
     * Constructor for the Deck.
     * It initializes a standard 52-card deck with one of each Rank and Suit combination.
     */
    public Deck() {
        super(52); // A standard deck has 52 cards
        // Iterate through all Suits
        for (Suit suit : Suit.values()) {
            // Iterate through all Ranks
            for (Rank rank : Rank.values()) {
                // Add a new card to the deck's list
                getCards().add(new BlackjackCard(rank, suit));
            }
        }
    }

    /**
     * Deals the top card from the deck.
     * Removes the card from the deck's list.
     * @return The top card, or null if the deck is empty.
     */
    public BlackjackCard dealCard() {
        if (getCards().isEmpty()) {
            return null; // Or throw an exception if you prefer
        }
        // Remove and return the last card (top of the deck)
        return (BlackjackCard) getCards().remove(getCards().size() - 1);
    }
}