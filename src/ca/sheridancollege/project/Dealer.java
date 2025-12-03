/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ca.sheridancollege.project;

/**
 *
 * @author win 11
 */
public class Dealer extends Player {

    private Hand hand;

    /**
     * Constructor for the Dealer.
     * @param name The dealer's name (e.g., "Dealer").
     */
    public Dealer(String name) {
        super(name);
        this.hand = new Hand();
    }

    public Hand getHand() {
        return hand;
    }

    /**
     * Resets the dealer's hand for a new round.
     */
    public void resetHand() {
        this.hand = new Hand();
    }

    /**
     * Implementation of the abstract play method.
     * This is a placeholder. The dealer's automated play logic (hit to 16, stand on 17)
     * will be implemented within the BlackjackGame class.
     */
    @Override
    public void play() {
        // Logic for dealer turn will be handled in the BlackjackGame class
    }
    
    /**
     * Returns the dealer's face-up card.
     * This is typically the second card dealt to the dealer.
     * @return The face-up card, or null if no cards are dealt.
     */
    public BlackjackCard getFaceUpCard() {
        if (hand.getCards().size() >= 2) {
            // The first card (index 0) is the hole card, the second (index 1) is the face-up card.
            return (BlackjackCard) hand.getCards().get(1);
        }
        return null;
    }
}
