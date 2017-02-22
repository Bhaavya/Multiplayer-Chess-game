package GameLogic.Board;
import GameLogic.Piece.*;
import GameLogic.Player.Player;
import java.util.*;
/**Defines setup for a standard 8*8 square board
 *
 */
public class SquareBoard extends Board {
    public SquareBoard(int size) {
        super(size, size);
    }

    //mapping for specifying initial piece at the cells
    public HashMap<String,Piece.pieceType> config= new HashMap<>();
    /** create a mapping from cell to its initial piece
     *
     * @param piece
     * @param cell
     */
    public void setConfig(Piece.pieceType piece,String cell) {
        config.put(cell, piece);
    }

    /** Determine piece at cell during board setup
     *
     */
    public Piece getInitialPieceAtCell(Cell cell) {
        Piece pieceAtCell;
        String colorOfPiece;
        List<Integer> whiteRanks=Arrays.asList(0,1);
        Piece.pieceType piece;
        int rank;
        if (whiteRanks.contains(cell.rank)) {
            colorOfPiece = "white";
        }
        else {
            colorOfPiece = "black";
        }
        piece = config.get(Integer.toString(cell.rank)+Integer.toString(cell.file));
        switch(piece) {
            case BLACK_BISHOP:
            case WHITE_BISHOP:
                pieceAtCell = new Bishop(colorOfPiece, cell);
                break;
            case BLACK_KING:
            case WHITE_KING:
                pieceAtCell = new King(colorOfPiece, cell);
                break;
            case BLACK_KNIGHT:
            case WHITE_KNIGHT:
                pieceAtCell = new Knight(colorOfPiece, cell);
                break;
            case BLACK_PAWN:
            case WHITE_PAWN:
                pieceAtCell = new Pawn(colorOfPiece, cell);
                break;
            case BLACK_QUEEN:
            case WHITE_QUEEN:
                pieceAtCell = new Queen(colorOfPiece, cell);
                break;
            case BLACK_ROOK:
            case WHITE_ROOK:
                pieceAtCell = new Rook(colorOfPiece, cell);
                break;
            case BLACK_EMPRESS:
            case WHITE_EMPRESS:
                pieceAtCell = new Empress(colorOfPiece, cell);
                break;
            case WHITE_PRINCESS:
            case BLACK_PRINCESS:
                pieceAtCell = new Princess(colorOfPiece, cell);
                break;
            default:
                pieceAtCell = new Grasshopper(colorOfPiece, cell);
                break;
        }
        return pieceAtCell;
    }

    /** get piece at cell in a board at any instance
     *
     * @param pieceAtCell
     * @param cell
     * @param pieceColor
     * @return
     */
    public Piece getPieceType(Piece pieceAtCell,Cell cell,String pieceColor){
        Piece tempPiece;
        if(pieceAtCell instanceof King){
            Piece king = new King(pieceColor,cell);
            tempPiece=king;
        }
        else if(pieceAtCell instanceof  Queen){
            Piece queen = new Queen(pieceColor,cell);
            tempPiece=queen;
        }
        else if(pieceAtCell instanceof Bishop){
            Piece bishop = new Bishop(pieceColor,cell);
            tempPiece=bishop;
        }
        else if(pieceAtCell instanceof Knight){
            Piece knight = new Knight(pieceColor,cell);
            tempPiece=knight;
        }
        else if(pieceAtCell instanceof Pawn){
            Piece pawn = new Pawn(pieceColor,cell);
            tempPiece=pawn;
        }
        else if(pieceAtCell instanceof Empress){
            Piece empress = new Empress(pieceColor, cell);
            tempPiece = empress;
        }
        else if(pieceAtCell instanceof Rook){
            Piece rook = new Rook(pieceColor, cell);
            tempPiece = rook;
        }
        else if(pieceAtCell instanceof Grasshopper){
            Piece rook = new Rook(pieceColor, cell);
            tempPiece = rook;
        }
        else{
            Piece princess = new Princess(pieceColor, cell);
            tempPiece = princess;
        }
        return tempPiece;
    }
    @Override
    public Board tempBoard(Board board,Player p1,Player p2, Player p3,Player p4) {
        SquareBoard newBoard=new SquareBoard(board.numRanks);
        newBoard.cells=new Cell[board.numRanks][board.numFiles];
        String player1Color=p1.getPlayerColor();
        ArrayList <Piece> player3AvailablePieces=new ArrayList<>();
        ArrayList <Piece> player4AvailablePieces=new ArrayList<>();
        for (int rank=0;rank<board.numRanks;rank++) {
                for (int file=0; file<board.numFiles;file++) {
                    newBoard.cells[rank][file] = new Cell(rank, file);
                    if (!(board.cells[rank][file].isOccupied))
                        newBoard.cells[rank][file].isOccupied = false;
                    else {
                        newBoard.cells[rank][file].isOccupied=true;
                        Cell cell=cells[rank][file];
                        Piece pieceAtCell= cell.getPieceAtCell();
                        String pieceColor=pieceAtCell.getPieceColor();
                        Piece tempPiece=getPieceType(pieceAtCell, newBoard.cells[rank][file],pieceColor);
                        setPlayerAndCellParameters(newBoard, rank, file, player3AvailablePieces, player4AvailablePieces, tempPiece, player1Color, pieceColor);
                    }
                }
            }
            p3.setAvailablePieces(player3AvailablePieces);
            p4.setAvailablePieces(player4AvailablePieces);
            return newBoard;
    }
    /** set available pieces for the copy of players
     *
     */
    public void setPlayerAndCellParameters(Board b, int rank,int file, ArrayList<Piece> player3AvailablePieces, ArrayList<Piece> player4AvailablePieces, Piece tempPiece,String pieceColor,String player1color) {
        Cell cell=b.cells[rank][file] ;
        cell.setPiece(tempPiece);
        tempPiece.setCurrentCell(cell) ;
        if (pieceColor.equals(player1color))
            player3AvailablePieces.add(tempPiece);
        else
            player4AvailablePieces.add(tempPiece);
    }

    public void setBoard(Player white, Player black) {
        List<Integer> occupiedRanks = Arrays.asList(0, 1, 6, 7);
        ArrayList<Piece> whitePieces = new ArrayList<>();
        ArrayList<Piece> blackPieces = new ArrayList<>();

        for (int rank = 0; rank < numRanks; rank++) {
            for (int file = 0; file < numFiles; file++) {
                cells[rank][file] = new Cell(rank, file);
                if (occupiedRanks.contains(rank)) {
                    Piece pieceAtCell;
                    pieceAtCell = getInitialPieceAtCell(cells[rank][file]);
                    if (pieceAtCell.getPieceColor().equals("white"))
                        whitePieces.add(pieceAtCell);
                    else
                        blackPieces.add(pieceAtCell);
                    cells[rank][file].setPiece(pieceAtCell);
                    pieceAtCell.setCurrentCell(cells[rank][file]);
                }
            }
            white.setAvailablePieces(whitePieces);
            black.setAvailablePieces(blackPieces);
            setBlackPieces(blackPieces);
            setWhitePieces(whitePieces);
        }
    }
    /**Change initial pieces on board
     *
     * @param pieceConfig 2D array specifying initial pieces at ranks 1,2,7 and 8
     */
    public void modifyConfig(Piece.pieceType[][] pieceConfig){
        //set configuration for initial position of white pieces
        for(int rank=0;rank<2;rank++) {
            for (int file = 0; file < numFiles; file++) {
                setConfig(pieceConfig[rank][file], Integer.toString(rank)+Integer.toString(file));
            }
        }

        for(int rank=6;rank<8;rank++) {
            for (int file = 0; file < numFiles; file++) {
                setConfig(pieceConfig[rank-4][file], Integer.toString(rank)+Integer.toString(file));
            }
        }
    }

    public HashMap<String, Piece.pieceType> getConfig(){return config;}
}
