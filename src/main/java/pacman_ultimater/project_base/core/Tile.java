package pacman_ultimater.project_base.core;

import java.awt.*;

/**
  * Class provides resources for saving text symbols as tiles and easier further manipulation.
  * Also contains useful function for converting selected keywords to tiles.
  */
public class Tile
{
    public static final int DEFPENSIZE = 2;

    public nType tile;
    public static int tileSize = LoadMap.TILESIZEINPXS;
    public static int dotSize = tileSize / 4;
    public static int penSize = DEFPENSIZE;

    /**
     * Enumerable for possible tile states.
     * HTBDTILE = Horizontal To Be Decided Tile, VTBDTILE = Vertical, etc..
     */
    public enum nType { DOT, POWERDOT, FREE, GATE, LWALLDOUBLE, RWALLDOUBLE,
        TWALLDOUBLE, BWALLDOUBLE, LWALLSINGLE, RWALLSINGLE, TWALLSINGLE,
        BWALLSINGLE, TLCURVEDOUBLE, TRCURVEDOUBLE, BRCURVEDOUBLE, BLCURVEDOUBLE,
        TLCURVESINGLE, TRCURVESINGLE, BRCURVESINGLE, BLCURVESINGLE, HTBDTILE, VTBDTILE, TILE }

    /**
     * Function for conversion between string representation of tile states and enumerable.
     * Returns corresponding tile state.
     *
     * @param tile string representation of tile state.
     */
    Tile(String tile)
    {
        switch (tile)
        {
            case " ":
                this.tile = nType.FREE;
                break;
            case "X":
                this.tile = nType.GATE;
                break;
            case ".":
                this.tile = nType.DOT;
                break;
            case "o":
                this.tile = nType.POWERDOT;
                break;
            case "DWL":
                this.tile = nType.LWALLDOUBLE;
                break;
            case "DWR":
                this.tile = nType.RWALLDOUBLE;
                break;
            case "DWT":
                this.tile = nType.TWALLDOUBLE;
                break;
            case "DWB":
                this.tile = nType.BWALLDOUBLE;
                break;
            case "SWL":
                this.tile = nType.LWALLSINGLE;
                break;
            case "SWR":
                this.tile = nType.RWALLSINGLE;
                break;
            case "SWT":
                this.tile = nType.TWALLSINGLE;
                break;
            case "SWB":
                this.tile = nType.BWALLSINGLE;
                break;
            case "DCTL":
                this.tile = nType.TLCURVEDOUBLE;
                break;
            case "DCTR":
                this.tile = nType.TRCURVEDOUBLE;
                break;
            case "DCBR":
                this.tile = nType.BRCURVEDOUBLE;
                break;
            case "DCBL":
                this.tile = nType.BLCURVEDOUBLE;
                break;
            case "SCTL":
                this.tile = nType.TLCURVESINGLE;
                break;
            case "SCTR":
                this.tile = nType.TRCURVESINGLE;
                break;
            case "SCBR":
                this.tile = nType.BRCURVESINGLE;
                break;
            case "SCBL":
                this.tile = nType.BLCURVESINGLE;
                break;
            case "VTBDTILE":
                this.tile = nType.VTBDTILE;
                break;
            case "HTBDTILE":
                this.tile = nType.HTBDTILE;
                break;
            default:
                this.tile = nType.TILE;
                break;
        }
    }

    /**
     * @param tileType Source tile type.
     */
    public Tile(nType tileType)
    {
        this.tile = tileType;
    }

    /**
     * Function for drawing tile states as their curve representation.
     *
     * @param g Current graphics handler.
     * @param location Tile's top left corner.
     * @param color Desired color of tile's curve representation.
     */
    public void DrawTile(Graphics g, Point location, Color color)
    {
        switch (tile)
        {
            case DOT:
                Dot(g, location); break;
            case POWERDOT:
                PowerDot(g, location);  break;
            case GATE:
                Gate(g, location); break;
            case LWALLDOUBLE:
                LWallDouble(g, location, color); break;
            case RWALLDOUBLE:
                RWallDouble(g, location, color); break;
            case TWALLDOUBLE:
                TWallDouble(g, location, color); break;
            case BWALLDOUBLE:
                BWallDouble(g, location, color); break;
            case LWALLSINGLE:
                LWallSingle(g, location, color); break;
            case RWALLSINGLE:
                RWallSingle(g, location, color); break;
            case TWALLSINGLE:
                TWallSingle(g, location, color); break;
            case BWALLSINGLE:
                BWallSingle(g, location, color); break;
            case TLCURVEDOUBLE:
                TLCurveDouble(g, location, color); break;
            case TRCURVEDOUBLE:
                TRCurveDouble(g, location, color); break;
            case BRCURVEDOUBLE:
                BRCurveDouble(g, location, color); break;
            case BLCURVEDOUBLE:
                BLCurveDouble(g, location, color); break;
            case TLCURVESINGLE:
                TLCurveSingle(g, location, color); break;
            case TRCURVESINGLE:
                TRCurveSingle(g, location, color); break;
            case BRCURVESINGLE:
                BRCurveSingle(g, location, color); break;
            case BLCURVESINGLE:
                BLCurveSingle(g, location, color); break;
            default:
                break;
        }
    }

    /**
     * Visually frees tile by drawing rectangle over it, with the color of background.
     *
     * @param g Current graphics handler.
     * @param location Tile's top left corner.
     * @param bkgColor Color of map's background.
     */
    public void FreeTile(Graphics g, Point location, Color bkgColor)
    {
        g.setColor(bkgColor);
        g.fillRect(location.x, location.y, tileSize, tileSize);
    }

    //<editor-fold desc="- TILE STATES Curves representations -">

    /**
     * Draw collectible dot.
     *
     * @param g Current graphics handler.
     * @param location Tile's top left corner.
     */
    private void Dot(Graphics g, Point location)
    {
        g.setColor(new Color(246, 172, 152));
        g.fillRect(location.x + 6, location.y + 6, dotSize, dotSize);
    }

    /**
     * Draw power pellet.
     *
     * @param g Current graphics handler.
     * @param location Tile's top left corner.
     */
    private void PowerDot(Graphics g, Point location)
    {
        g.setColor(new Color(246, 172, 152));
        g.fillOval(location.x, location.y, tileSize, tileSize);
    }

    /**
     * Draw ghost house gate.
     *
     * @param g Current graphics handler.
     * @param location Tile's top left corner.
     */
    private void Gate(Graphics g, Point location)
    {
        g.setColor(new Color(245, 187, 229));
        g.fillRect(location.x, location.y + 10, tileSize, dotSize);
    }

    /**
     * Draw left double-lined wall.
     *
     * @param g Current graphics handler.
     * @param location Tile's top left corner.
     * @param color Wall color.
     */
    private void LWallDouble(Graphics g, Point location, Color color)
    {
        g.setColor(color);
        g.fillRect(location.x + 1, location.y - 1, penSize, tileSize + 2);
        g.fillRect(location.x + 7, location.y - 1, penSize, tileSize + 2);
    }

    /**
     * Draw left single-lined wall.
     *
     * @param g Current graphics handler.
     * @param location Tile's top left corner.
     * @param color Wall color.
     */
    private void LWallSingle(Graphics g, Point location, Color color)
    {
        g.setColor(color);
        g.fillRect(location.x + 7, location.y, penSize, tileSize);
    }

    /**
     * Draw right double-lined wall.
     *
     * @param g Current graphics handler.
     * @param location Tile's top left corner.
     * @param color Wall color.
     */
    private void RWallDouble(Graphics g, Point location, Color color)
    {
        g.setColor(color);
        g.fillRect(location.x + 13, location.y - 1, penSize, tileSize + 2);
        g.fillRect(location.x + 7, location.y - 1, penSize, tileSize + 2);
    }

    /**
     * Draw right single-lined wall.
     *
     * @param g Current graphics handler.
     * @param location Tile's top left corner.
     * @param color Wall color.
     */
    private void RWallSingle(Graphics g, Point location, Color color)
    {
        g.setColor(color);
        g.fillRect(location.x + 7, location.y, penSize, tileSize);
    }

    /**
     * Draw top double-lined wall.
     *
     * @param g Current graphics handler.
     * @param location Tile's top left corner.
     * @param color Wall color.
     */
    private void TWallDouble(Graphics g, Point location, Color color)
    {
        g.setColor(color);
        g.fillRect(location.x - 1, location.y + 1, tileSize + 2, penSize);
        g.fillRect(location.x - 1, location.y + 7, tileSize + 2, penSize);
    }

    /**
     * Draw top single-lined wall.
     *
     * @param g Current graphics handler.
     * @param location Tile's top left corner.
     * @param color Wall color.
     */
    private void TWallSingle(Graphics g, Point location, Color color)
    {
        g.setColor(color);
        g.fillRect(location.x, location.y + 7, tileSize, penSize);
    }

    /**
     * Draw bottom double-lined wall.
     *
     * @param g Current graphics handler.
     * @param location Tile's top left corner.
     * @param color Wall color.
     */
    private void BWallDouble(Graphics g, Point location, Color color)
    {
        g.setColor(color);
        g.fillRect(location.x - 1, location.y + 13, tileSize + 2, penSize);
        g.fillRect(location.x - 1, location.y + 7, tileSize + 2, penSize);
    }

    /**
     * Draw bottom single-lined wall.
     *
     * @param g Current graphics handler.
     * @param location Tile's top left corner.
     * @param color Wall color.
     */
    private void BWallSingle(Graphics g, Point location, Color color)
    {
        g.setColor(color);
        g.fillRect(location.x, location.y + 7, tileSize, penSize);
    }

    /**
     * Draw top left double-lined curve.
     *
     * @param g Current graphics handler.
     * @param location Tile's top left corner.
     * @param color Wall color.
     */
    private void TLCurveDouble(Graphics g, Point location, Color color)
    {
        g.setColor(color);
        g.drawArc(location.x + 2, location.y + 2,24,24,90,90);
        g.drawArc(location.x + 8, location.y + 8,14,14,90,90);
    }

    /**
     * Draw top left single-lined curve.
     *
     * @param g Current graphics handler.
     * @param location Tile's top left corner.
     * @param color Wall color.
     */
    private void TLCurveSingle(Graphics g, Point location, Color color)
    {
        g.setColor(color);
        g.drawArc(location.x + 8, location.y + 8, 14, 14, 90, 90);
    }

    /**
     * Draw top right double-lined curve.
     *
     * @param g Current graphics handler.
     * @param location Tile's top left corner.
     * @param color Wall color.
     */
    private void TRCurveDouble(Graphics g, Point location, Color color)
    {
        g.setColor(color);
        g.drawArc(location.x - 10,location.y + 2,24,24, 90 ,-90);
        g.drawArc(location.x - 6, location.y + 8, 14, 14, 90 ,-90);
    }

    /**
     * Draw top right single-lined curve.
     *
     * @param g Current graphics handler.
     * @param location Tile's top left corner.
     * @param color Wall color.
     */
    private void TRCurveSingle(Graphics g, Point location, Color color)
    {
        g.setColor(color);
        g.drawArc(location.x - 6, location.y + 8, 14, 14, 90 , -90);
    }

    /**
     * Draw bottom right double-lined curve.
     *
     * @param g Current graphics handler.
     * @param location Tile's top left corner.
     * @param color Wall color.
     */
    private void BRCurveDouble(Graphics g, Point location, Color color)
    {
        g.setColor(color);
        g.drawArc(location.x - 6, location.y - 6, 14,14, 0, -90);
        g.drawArc(location.x - 10, location.y - 10, 24,24, 0, -90);
    }

    /**
     * Draw bottom right single-lined curve.
     *
     * @param g Current graphics handler.
     * @param location Tile's top left corner.
     * @param color Wall color.
     */
    private void BRCurveSingle(Graphics g, Point location, Color color)
    {
        g.setColor(color);
        g.drawArc(location.x - 6,location.y - 6,14,14,0,-90);
    }

    /**
     * Draw bottom left double-lined curve.
     *
     * @param g Current graphics handler.
     * @param location Tile's top left corner.
     * @param color Wall color.
     */
    private void BLCurveDouble(Graphics g, Point location, Color color)
    {
        g.setColor(color);
        g.drawArc(location.x + 2,location.y - 10,24,24,180,90);
        g.drawArc(location.x + 8,location.y - 6,14,14,180,90);
    }

    /**
     * Draw bottom left single-lined curve.
     *
     * @param g Current graphics handler.
     * @param location Tile's top left corner.
     * @param color Wall color.
     */
    private void BLCurveSingle(Graphics g, Point location, Color color)
    {
        g.setColor(color);
        g.drawArc(location.x + 8, location.y - 6, 14, 14, 180,90);
    }

    //</editor-fold>
}
