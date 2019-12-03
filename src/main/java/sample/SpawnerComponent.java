package sample;

import com.almasb.fxgl.entity.component.Component;
import javafx.geometry.Point2D;

public class SpawnerComponent extends Component {
    Point2D coordinates;

    public SpawnerComponent(double x, double y) {
        coordinates = new Point2D(x, y);
    }
}
