package backEnd;

import util.Point2d;
import util.Vector2d;

public interface AttractSource {

	public Vector2d getAttractionVectorFromPoint(Point2d point);
	
}
