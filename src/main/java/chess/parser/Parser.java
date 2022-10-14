package chess.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.AbstractMap.SimpleEntry;

import chess.model.rules.action.Action;
import chess.model.rules.action.ActionType;
import chess.model.rules.Condition;
import chess.model.board.Board;
import chess.model.board.Coordinate;
import chess.model.piece.PieceBehavior;
import chess.model.piece.PieceType;
import chess.model.piece.PieceTypeID;
import chess.model.piece.Player;
import chess.model.rules.MovementPattern;
import chess.model.rules.property.Property;
import chess.model.rules.property.PropertyType;
import chess.utils.InstanceFactory;

import static chess.parser.Constants.*;

public class Parser {

    private final Map<String, SymbolMapper> functionTable;
    private final Map<Character, SymbolMapper> symbolTable;
    private final Map<Character, PieceBehavior> pieceTable;
    private final Map<String, Condition> conditionTable;
    private final Map<String, Coordinate> coordinateTable;
    private final Map<String, MovementPattern> vectorTable;
    private final Map<String, Property> propertyTable;
    private final Map<String, Action> actionTable;
    private final Map<ActionType, String> actionClassReflector;
    private final Map<PropertyType, String> propertyClassReflector;
    private String fen;

    /**
     * The parser reads in a game file outling the pieces, 
     * piece configuration, piece behavior, and piece side effect actions.
     * The parser saves the user defined symbols in a table and uses them
     * to construct the abstract Java representation of the game
     */
    public Parser() {
        functionTable = new HashMap<>();
        symbolTable = new HashMap<>();
        pieceTable = new HashMap<>();
        vectorTable = new HashMap<>();
        coordinateTable = new HashMap<>();
        conditionTable = new HashMap<>();
        propertyTable = new HashMap<>();
        actionTable = new HashMap<>();
        actionClassReflector = new HashMap<>();
        propertyClassReflector = new HashMap<>();
        initFunctionTable();
        initSymbolTable();
        initActionClassReflector();
        initPropertyClassReflector();
    }

    /**
     * Loads a game with a specified path and parses line by line
     * The contents is saved into mapping tables which is used to
     * construct the game abstract representation
     * @param path Path of the game file
     * @throws IOException file not found
     */
    public void loadGameFile(String path) throws IOException {
        InputStream is = Parser.class.getResourceAsStream(path);
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String line;
        while ((line = br.readLine()) != null) {
            processLine(line);
        }
        br.close();
    }

    /**
     * Creates a new Board object with pieces placed on the board
     * in accordance to the standard FEN notation system
     * @return Board
     */
    public Board generateBoard() {
        Board b = new Board();
        FenParser f = new FenParser(pieceTable);
        b.initBoard(f.getDimensions(fen), f.configurePieces(fen));
        return b;
    }

    private void processLine(String line) {
        if (line.length() == 0) return;
        if (symbolTable.containsKey(line.trim().charAt(0))) symbolTable.get(line.trim().charAt(0)).symbMap(line);
        else if (line.trim().charAt(1) == DOT_OPERATOR) functionTable.get(line.trim().substring(2, line.trim().indexOf(LEFT_PAREN))).symbMap(line);
    }

    private void pieceDeclaration(String line) {
        char piece = line.charAt(line.indexOf(PIECE_DECLARATOR) + 1);
        char symbol = getSymbol(line, piece);
        pieceTable.put(piece, new PieceBehavior(new PieceTypeID(Player.WHITE, piece, symbol)));
        pieceTable.put((char)(piece + ASCII_CASE_OFFSET), new PieceBehavior(new PieceTypeID(Player.BLACK, piece, symbol)));
    }

    private void layout(String line) {
        int declaration = line.indexOf(LAYOUT_DECLARATOR) + 1;
        fen = line.substring(declaration).trim();
    }

    private void vectorDeclaration(String line) {
        int declaration = line.indexOf(VECTOR_DECLARATOR) + 1;
        int operator = line.indexOf(ASSIGNMENT_OPERATOR);
        String vectorSymbol = line.substring(declaration, operator).trim();
        String[] args = getArgs(line, LEFT_BRACKET, RIGHT_BRACKET);
        vectorTable.put(vectorSymbol, new MovementPattern(stoi(args[0]), stoi(args[1]), stoi(args[2])));
    }

    private void coordinateDeclaration(String line) {
        int declaration = line.indexOf(COORDINATE_DECLARATOR) + 1;
        int operator = line.indexOf(ASSIGNMENT_OPERATOR);
        String coordinateSymbol = line.substring(declaration, operator).trim();
        String[] args = getArgs(line, LEFT_BRACKET, RIGHT_BRACKET);
        coordinateTable.put(coordinateSymbol, new Coordinate(stoi(args[0]), stoi(args[1])));
    }

    private void conditionDeclaration(String line) {
        int declaration = line.indexOf(CONDITION_DECLARATOR) + 1;
        int operator = line.indexOf(ASSIGNMENT_OPERATOR);
        String conditionSymbol = line.substring(declaration, operator).trim();
        conditionTable.putIfAbsent(conditionSymbol, new Condition(new HashMap<>(), new HashMap<>(), new HashMap<>()));
        String[] args = getArgs(line, LEFT_PAREN, RIGHT_PAREN);
        args = reconstructedArgs(args);
        Optional<SimpleEntry<PieceType, Coordinate>> absoluteCondition = parseConditionEntry(args[0].trim(), coordinateTable);
        Optional<SimpleEntry<PieceType, MovementPattern>> relativeCondition = parseConditionEntry(args[1].trim(), vectorTable);
        Optional<SimpleEntry<PieceType, Property>> propertyCondition = parseConditionEntry(args[2].trim(), propertyTable);
        absoluteCondition.ifPresent(e -> conditionTable.get(conditionSymbol).addAbsoluteCondition(e));
        relativeCondition.ifPresent(e -> conditionTable.get(conditionSymbol).addRelativeCondition(e));
        propertyCondition.ifPresent(e -> conditionTable.get(conditionSymbol).addPropertyCondition(e));
    }

    private void propertyDeclaration(String line) {
        int declaration = line.indexOf(PROPERTY_DECLARATOR) + 1;
        int operator = line.indexOf(ASSIGNMENT_OPERATOR);
        String propertySymbol = line.substring(declaration, operator).trim();
        String[] args = getArgs(line, LEFT_BRACKET, RIGHT_BRACKET);
        PropertyType pt = PropertyType.valueOf(((String)args[0]).trim());
        Class<?>[] paramTypes = {PropertyType.class, List.class};
        Object[] params = {pt, propertiesLister(args)};
        Object p = null;
        try {
            p = InstanceFactory.createInstance(propertyClassReflector.get(pt), paramTypes, params);
        } catch (IOException e) {
            e.printStackTrace();
        }
        propertyTable.put(propertySymbol, Property.class.cast(p));
    }

    private void actionDeclaration(String line) {
        int declaration = line.indexOf(ACTION_DECLARATOR) + 1;
        int operator = line.indexOf(ASSIGNMENT_OPERATOR);
        String actionSymbol = line.substring(declaration, operator).trim();
        Object[] args = getObjectArgs(line);
        ActionType at = ActionType.valueOf(((String)args[0]).trim());
        Class<?>[] paramTypes = {ActionType.class, List.class};
        Object[] params = {at, propertiesLister(args)};
        Object a = null;
        try {
            a = InstanceFactory.createInstance(actionClassReflector.get(at), paramTypes, params);
        } catch (IOException e) {
            e.printStackTrace();
        }
        actionTable.put(actionSymbol, Action.class.cast(a));
    }

    private String[] reconstructedArgs(String[] args) {
        String[] reconstructedArgs = new String[3];
        int fixedArgs = 0;
        for (int i = 0; i < args.length; i++) {
            if (args[i].trim().charAt(0) == LEFT_CARROT) {
                reconstructedArgs[fixedArgs++] = args[i] + COMMA + args[i+1];
                i++;
            }
            else reconstructedArgs[fixedArgs++] = args[i];
        }
        return reconstructedArgs;
    }

    private Optional<String[]> parseCondition(String args) {
        int l = args.indexOf(LEFT_CARROT);
        int r = args.indexOf(RIGHT_CARROT);
        if (l < 0 || r < 0) return Optional.empty();
        String[] parsedArgs = getArgs(args, LEFT_CARROT, RIGHT_CARROT);
        return Optional.of(parsedArgs);
    }

    private <T> Optional<SimpleEntry<PieceType, T>> parseConditionEntry(String args, Map<String, T> table) {
        Optional<String[]> conditionArgs = parseCondition(args);
        if (conditionArgs.isEmpty()) return Optional.empty();
        String key = conditionArgs.get()[0].trim();
        String value = conditionArgs.get()[1].trim();
        return Optional.of(new AbstractMap.SimpleEntry<>(determinePieceType(key), table.get(value)));
    }

    private PieceType determinePieceType(String token) {
        PieceType p = new PieceType();
        if (token.charAt(0) == ANY) p.all = true;
        if (token.charAt(0) == FRIEND) p.friendly = true;
        if (token.charAt(0) == ENEMY) p.enemy = true;
        if (token.charAt(0) == SELF) p.selfInstance = true;
        if (vectorTable.containsKey(token)) p.relativeNeighbor = vectorTable.get(token);
        if (vectorTable.containsKey(token.substring(1))) p.relativeNeighbor = vectorTable.get(token.substring(1));
        return p;
    }

    private char getSymbol(String line, Character piece) {
        char symbol = EMPTY_CHARACTER;
        if (line.indexOf(COLON) >= 0) {
            String[] args = getArgs(line, LEFT_BRACKET, RIGHT_BRACKET);
            if (args[0].length() > 0) symbol = args[0].charAt(0);
        }
        else {
            symbol = piece;
        }
        return symbol;
    }

    private void bothDoes(String line) {
        char piece = line.charAt(0);
        char complementPiece = (char)(piece + ASCII_CASE_OFFSET);
        String[] args = getArgs(line, LEFT_PAREN, RIGHT_PAREN);
        String[] arrangedArgs = {args[0], "", "", ""};
        assignBehavior(piece, arrangedArgs);
        assignBehavior(complementPiece, arrangedArgs);
    }

    private void does(String line) {
        char piece = line.charAt(0);
        String[] args = getArgs(line, LEFT_PAREN, RIGHT_PAREN);
        String[] arrangedArgs = {args[0], "", "", ""};
        assignBehavior(piece, arrangedArgs);
    }

    private void bothMust(String line) {
        char piece = line.charAt(0);
        char complementPiece = (char)(piece + ASCII_CASE_OFFSET);
        String[] args = getArgs(line, LEFT_PAREN, RIGHT_PAREN);
        String[] arrangedArgs = {args[0], args[1], "", ""};
        assignBehavior(piece, arrangedArgs);
        assignBehavior(complementPiece, arrangedArgs);
    }

    private void must(String line) {
        char piece = line.charAt(0);
        String[] args = getArgs(line, LEFT_PAREN, RIGHT_PAREN);
        String[] arrangedArgs = {args[0], args[1], "", ""};
        assignBehavior(piece, arrangedArgs);
    }

    private void bothCannot(String line) {
        char piece = line.charAt(0);
        char complementPiece = (char)(piece + ASCII_CASE_OFFSET);
        String[] args = getArgs(line, LEFT_PAREN, RIGHT_PAREN);
        String[] arrangedArgs = {args[0], "", args[1], ""};
        assignBehavior(piece, arrangedArgs);
        assignBehavior(complementPiece, arrangedArgs);
    }

    private void cannot(String line) {
        char piece = line.charAt(0);
        String[] args = getArgs(line, LEFT_PAREN, RIGHT_PAREN);
        String[] arrangedArgs = {args[0], "", args[1], ""};
        assignBehavior(piece, arrangedArgs);
    }

    private void bothAction(String line) {
        char piece = line.charAt(0);
        char complementPiece = (char)(piece + ASCII_CASE_OFFSET);
        String[] args = getArgs(line, LEFT_PAREN, RIGHT_PAREN);
        String[] arrangedArgs = {args[0], "", "", args[1]};
        assignBehavior(piece, arrangedArgs);
        assignBehavior(complementPiece, arrangedArgs);
    }

    private void action(String line) {
        char piece = line.charAt(0);
        String[] args = getArgs(line, LEFT_PAREN, RIGHT_PAREN);
        String[] arrangedArgs = {args[0], "", "", args[1]};
        assignBehavior(piece, arrangedArgs);
    }

    private void bothCapture(String line) {

    }

    private void capture(String line) {

    }

    private void assignBehavior(char piece, String[] args) {
        MovementPattern mp = vectorTable.get(args[0].trim());
        Condition fc = conditionTable.get(args[1].trim());
        Condition ic = conditionTable.get(args[2].trim());
        Action a = actionTable.get(args[3].trim());
        if (mp == null) return;
        pieceTable.get(piece).addMovementPattern(mp);
        if (fc != null) pieceTable.get(piece).addFulfillCond(mp, fc);
        if (ic != null) pieceTable.get(piece).addInhibitoryCond(mp, ic);
        if (a != null) pieceTable.get(piece).addAction(mp, a);
    }

    private String[] getArgs(String s, Character left, Character right) {
        int argsBegin = s.indexOf(left) + 1;
        int argsEnd = s.indexOf(right);
        String argsString = s.substring(argsBegin, argsEnd).trim();
        return argsString.split("" + COMMA);
    }

    private Object[] getObjectArgs(String s) {
        int argsBegin = s.indexOf(Constants.LEFT_BRACKET) + 1;
        int argsEnd = s.indexOf(Constants.RIGHT_BRACKET);
        String argsString = s.substring(argsBegin, argsEnd).trim();
        String[] listArgsString = argsString.split("" + COMMA);
        Object[] objectArgs = new Object[listArgsString.length];
        for (int i = 0; i < listArgsString.length; i++) {
            listArgsString[i] = listArgsString[i].trim();
            if (vectorTable.get(listArgsString[i]) != null) objectArgs[i] = vectorTable.get(listArgsString[i]);
            else objectArgs[i] = listArgsString[i];
        }
        return objectArgs;
    }

    private void initSymbolTable() {
        symbolTable.put(PIECE_DECLARATOR, this::pieceDeclaration);
        symbolTable.put(LAYOUT_DECLARATOR, this::layout);
        symbolTable.put(VECTOR_DECLARATOR, this::vectorDeclaration);
        symbolTable.put(COORDINATE_DECLARATOR, this::coordinateDeclaration);
        symbolTable.put(CONDITION_DECLARATOR, this::conditionDeclaration);
        symbolTable.put(PROPERTY_DECLARATOR, this::propertyDeclaration);
        symbolTable.put(ACTION_DECLARATOR, this::actionDeclaration);
    }

    private void initFunctionTable() {
        functionTable.put(BOTH + DOT_OPERATOR + DOES, this::bothDoes);
        functionTable.put(BOTH + DOT_OPERATOR + MUST, this::bothMust);
        functionTable.put(BOTH + DOT_OPERATOR + CANNOT, this::bothCannot);
        functionTable.put(BOTH + DOT_OPERATOR + ACTION, this::bothAction);
        functionTable.put(BOTH + DOT_OPERATOR + CAPTURE, this::bothCapture);
        functionTable.put(DOES, this::does);
        functionTable.put(MUST, this::must);
        functionTable.put(CANNOT, this::cannot);
        functionTable.put(ACTION, this::action);
        functionTable.put(CAPTURE, this::capture);
    }

    private void initPropertyClassReflector() {
        propertyClassReflector.put(PropertyType.CHECK_FLAG, "chess.model.rules.property.CheckFlag");
        propertyClassReflector.put(PropertyType.TIMES_MOVED, "chess.model.rules.property.TimesMoved");
        propertyClassReflector.put(PropertyType.TURNS_AGO_MOVED, "chess.model.rules.property.TurnsAgoMoved");
    }

    private void initActionClassReflector() {
        actionClassReflector.put(ActionType.SET_FLAG, "chess.model.rules.action.SetFlag");
        actionClassReflector.put(ActionType.MOVE, "chess.model.rules.action.Move");
        actionClassReflector.put(ActionType.REMOVE, "chess.model.rules.action.Remove");
    }

    private int stoi(String s) {
        return Integer.parseInt(s.trim());
    }

    private List<Object> propertiesLister(Object[] args) {
        List<Object> properties = new ArrayList<>();
        for (int i = 1; i < args.length; i++) {
            if (args[i] instanceof String) args[i] = ((String)args[i]).trim();
            properties.add(args[i]);
        }
        return properties;
    }
    
}
