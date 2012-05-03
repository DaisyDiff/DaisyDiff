package org.outerj.daisy.diff.html.dom;

/**
 * This is an artificial text node whose sole purpose is to separate
 * text nodes, so that they cannot be treated as a continuous text flow
 * by the RangeDifferencer.
 * 
 * Such nodes will be created between two text nodes, when they really
 * are separate, e.g. in two successive table cells.
 * 
 * @author Carsten Pfeiffer <carsten.pfeiffer@gebit.de>
 */
public class SeparatingNode extends TextNode {

	public SeparatingNode(TagNode parent) {
		super(parent, ""); //$NON-NLS-1$
	}
	
	@Override
	public boolean equals(Object other) {
		// No other separator is equal to this one. This has the effect
		// that text nodes separated by such a separator can never be
		// treated as a text sequence by the RangeDifferencer/TextNodeComparator.
		
		if (other == this) {
			return true;
		}
		return false;
	}

}
