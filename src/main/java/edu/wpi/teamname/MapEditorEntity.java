package edu.wpi.teamname;

import edu.wpi.teamname.Database.Map.*;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

public class MapEditorEntity {

  @Getter @Setter NodeDaoImpl ndi;
  @Getter @Setter LocationDoaImpl ldi;
  @Getter @Setter EdgeDaoImpl edi;
  @Getter @Setter MoveDaoImpl mdi;

  @Getter @Setter List<Node> nodeList;
  @Getter @Setter List<Edge> edgeList;
  @Getter @Setter List<Location> locationList;
  @Getter @Setter List<Move> moveList;

  public MapEditorEntity() {
    this.ndi = NodeDaoImpl.getInstance();
    this.edi = EdgeDaoImpl.getInstance();
    this.ldi = LocationDoaImpl.getInstance();
    this.mdi = MoveDaoImpl.getInstance();
    this.nodeList = this.ndi.getAllNodes();
    this.edgeList = this.edi.getAllEdges();
    this.locationList = this.ldi.getAllLocations();
    this.moveList = this.mdi.getAllMoves();
  }
}
