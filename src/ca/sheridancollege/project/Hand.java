/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ca.sheridancollege.project;

import java.util.ArrayList;
/**
 *
 * @author win 11
 */
public class Hand extends GroupOfCards {

    /**
     * Constructor for a Hand. It initializes an empty GroupOfCards.
     * A typical Blackjack hand starts empty, so the size passed is just for initialization.
     */
    public Hand() {
        super(0); // Start with a group of size 0
    }

    /**
     * Adds a card to the hand.
     * @param card The BlackjackCard to add.
     */
    public void addCard(BlackjackCard card) {
        getCards().add(card);
    }

    /**
     * Calculates the total value of the hand.
     * This method correctly handles Aces, counting them as 11 unless it would
     * cause the total to exceed 21, in which case they are counted as 1.
     * @return The total numerical value of the hand.
     */
    public int calculateTotal() {
        int total = 0;
        int aceCount = 0;

        // First pass: Calculate the total with all Aces as 11
        for (Card card : getCards()) {
            if (card instanceof BlackjackCard) {
                BlackjackCard blackjackCard = (BlackjackCard) card;
                total += blackjackCard.getValue();
                if (blackjackCard.isAce()) {
                    aceCount++;
                }
            }
        }

        // Second pass: If total is over 21, convert Aces from 11 to 1 one by one
        while (total > 21 && aceCount > 0) {
            total -= 10; // 11 becomes 1, so we subtract 10
            aceCount--;
        }

        return total;
    }

    /**
     * Returns a string representation of the hand, including the total value.
     * Example: "[Ace of Spades, King of Hearts] (Total: 21)"
     * @return A formatted string describing the hand.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        ArrayList<Card> cards = getCards();
        for (int i = 0; i < cards.size(); i++) {
            sb.append(cards.get(i).toString());
            if (i < cards.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("] (Total: ").append(calculateTotal()).append(")");
        return sb.toString();
    }
}
