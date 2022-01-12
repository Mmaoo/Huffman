package com.example.huffman.huffman;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Huffman implements Serializable {

    public class Triple<A,B,C> implements Serializable{
        public A first;
        public B second;
        public C third;
        Triple(){}
        Triple(A first, B second, C third){
            this.first = first;
            this.second = second;
            this.third = third;
        }
    }

    private Tree tree;
    private Bitmap bitmap;

    public String encode(String text){
        Log.w("Huffman","Encode");
        String code = new String("");
        char c;
        List<Boolean> charCode;
        tree = new Tree();

        for(int i=0;i<text.length();i++){
            c = text.charAt(i);
            charCode = tree.getCode(c);
            code = code.concat(toBinaryString(charCode));
            tree.add(c);
        }
        return code;
    }

    public String toBinaryString(List<Boolean> bits){
        String out = new String("");
        for(boolean b: bits){
            if(b) out = out.concat("1");
            else out = out.concat("0");
        }
        return out;
    }

    public Bitmap drawTree(){
        if(tree == null) return null;
        Log.w("drawTree","drawTree");
        int width = 150;
        bitmap = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(5);
        paint.setTextSize((float)width/3);

        Node node;
        int level = tree.checkLevel(tree.nyt);
        Map<Node, PointF> coordinates = new HashMap<Node,PointF>();
        List<Float> vertPos = new ArrayList<Float>();

        float x = (float)bitmap.getWidth()/2, y = (float)bitmap.getHeight()/2;

        drawNodeChild(tree.root,coordinates,vertPos,width,paint);

        return bitmap;
    }

    public void drawNodeChild(Node node, Map<Node,PointF> coordinates, List<Float> vertPos, int width, Paint paint){
        if(node != null) {
            drawNodeChild(node.right,coordinates,vertPos,width,paint);
            float x,y;
            if(vertPos.size() == 0){
                x = (float)width/2;
            }else{
                x = vertPos.get(0)-(((float)width/100)*120);
            }
            y = (float)width/2 + (tree.checkLevel(node) * (((float)width/100)*140));
            coordinates.put(node,new PointF(x,y));
            bitmap = extendBitmap(bitmap,x,y,width,coordinates);
            PointF point = coordinates.get(node);
            drawNode(node,point.x,point.y,width,paint);
            vertPos.add(0,point.x);
            drawLinetoParent(node.right,coordinates,width,paint);
            drawNodeChild(node.left,coordinates,vertPos,width,paint);
            drawLinetoParent(node.left,coordinates,width,paint);
        }
    }

    private Bitmap extendBitmap(Bitmap bitmap, float x, float y, int width, Map<Node,PointF> coordinates){
        float left = 0;
        int newWidth = bitmap.getWidth();
        float top = 0;
        int newHeight = bitmap.getHeight();

        float deltaX = 0, deltaY = 0;
        Log.w("extendBitmap","oldBitmap: width="+bitmap.getWidth()+", height="+bitmap.getHeight());
        Log.w("extendBitmap","position: x="+x+", y="+y);
        if(x < 0 + ((float)width/2)){
            deltaX = (float)Math.ceil(x-((float)width/2));
            left = Math.abs(deltaX);
        }else if(x > bitmap.getWidth()-((float)width/2)){
            deltaX = (float)Math.ceil(x-(bitmap.getWidth()-((float)width/2)));
        }
        if(y < 0 + ((float)width/2)){
            deltaY = (float)Math.ceil(y-((float)width/2));
            top = Math.abs(deltaY);
        }else if(y > bitmap.getHeight()-((float)width/2)){
            deltaY = (float)Math.ceil(y-(bitmap.getHeight()-((float)width/2)));
        }
        newWidth = (int)(bitmap.getWidth() + Math.abs(deltaX));
        newHeight = (int)(bitmap.getHeight() + Math.abs(deltaY));
        Log.w("extendBitmap","deltaX="+deltaX+", deltaY="+deltaY);
        Set<Map.Entry<Node,PointF>> set = coordinates.entrySet();

        for(Map.Entry<Node,PointF> entry : set){
            entry.getValue().x += left;
            entry.getValue().y += top;
        }
        Log.w("extendBitmap","createNewBitmap: width="+newWidth+", height="+newHeight+", left="+left+", top="+top);
        //Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(),bitmap.getConfig());
        Bitmap newBitmap = Bitmap.createBitmap(newWidth,newHeight,bitmap.getConfig());
        Canvas canvas = new Canvas(newBitmap);
        //canvas.drawBitmap(bitmap,0,0,null);
        canvas.drawBitmap(bitmap,left,top,null);
        //canvas.drawLine(0,0, 100,100,new Paint());
        return newBitmap;
    }

    private void drawNode(Node node,float x, float y, int width, Paint paint){
        if(node != null) {
            Canvas canvas = new Canvas(bitmap);
            canvas.drawCircle(x, y, (float) width / 2, paint);
            paint.setColor(Color.WHITE);
            canvas.drawCircle(x, y, ((float) width / 2) - paint.getStrokeWidth(), paint);
            paint.setColor(Color.BLACK);
            canvas.drawLine(x, y - ((float) width / 2), x, y + ((float) width / 2), paint);
            if (node.getCharacter() != null) {
                switch (node.getCharacter().charValue()) {
                    case ' ' : canvas.drawText("[S]", x - 60, y + 15, paint); break;
                    case '\n' : canvas.drawText("[E]", x - 60, y + 15, paint); break;
                    default: canvas.drawText(node.getCharacter().toString(), x - 50, y + 15, paint);
                }
            }
            int weightNumCount = 0;
            int temp = node.getWeight();
            while(temp >= 10){ weightNumCount++; temp /= 10; }
            weightNumCount *= 10;
            if(weightNumCount < 10) weightNumCount = 10;
            paint.setTextSize(((float)width/3)-weightNumCount);
            canvas.drawText(Integer.toString(node.getWeight()), x + 10, y + 15, paint);
            paint.setTextSize((float)width/3);
        }
    }

    private void drawLinetoParent(Node node, Map<Node,PointF> coordinates, int width, Paint paint){
        if(node != null && node.parent != null) {
            Canvas canvas = new Canvas(bitmap);
            PointF coordNode = coordinates.get(node);
            PointF coordParent = coordinates.get(node.parent);

            float nx = coordNode.x;
            float ny = coordNode.y - ((float) width / 2);
            float px = coordParent.x;
            float py = coordParent.y + ((float) width / 2);

            canvas.drawLine(nx, ny, px, py, paint);

            // print 0/1
            float oix = Math.abs(nx-px);
            float oiy = py+(Math.abs(ny-py)/2)-5;
            if(node.parent.left == node){
                oix = px-(oix/2)-5-paint.getTextSize();
                canvas.drawText("1",oix,oiy,paint);
            }else{
                oix = px+(oix/2)+5;
                canvas.drawText("0",oix,oiy,paint);
            }
        }
    }

//    public Bitmap getBitmap() {
//        return bitmap;
//    }

    public float entropy() {
        float result = 0;
        int sum = tree.root.getWeight();
        for(Map.Entry<Character, Node> entry :
                tree.map.entrySet()) {
            Node node = entry.getValue();
            float p = (float)node.getWeight()/sum;
            result += p * (Math.log(1/p) / Math.log(2));
        }
        return result;
    }

    public float codeLengthMean() {
        float result = 0;
        int sum = tree.root.getWeight();
        for(Map.Entry<Character, Node> entry :
                tree.map.entrySet()) {
            Node node = entry.getValue();
            Character c = entry.getKey();
            float p = (float)node.getWeight()/sum;
            List<Boolean> charCode = tree.getCode(c);
            result += p * charCode.size();
        }
        return result;
    }

    public ArrayList<Triple<String, List<Boolean>, Integer>> getCodeBook(){
        ArrayList<Triple<String, List<Boolean>, Integer>> result = new ArrayList<>();
        for(Map.Entry<Character, Node> entry : tree.map.entrySet()) {
            Character c = entry.getKey();
            String s;
            switch(c.charValue()){
                case ' ' : s = "[Spacja]"; break;
                case '\n' : s = "[Enter]"; break;
                default: s = c.toString();
            }
            List<Boolean> code = tree.getCode(c);
            Integer w = entry.getValue().getWeight();
            result.add(new Triple<String, List<Boolean>, Integer>(s,code,w));
        }
        Collections.sort(result,new Comparator<Triple<String, List<Boolean>, Integer>>() {
            @Override
            public int compare(Triple<String, List<Boolean>, Integer> o1, Triple<String, List<Boolean>, Integer> o2) {
                return o2.third-o1.third;
            }
        });
        return result;
    }

//    public ArrayList<Pair<Character,String>> getCodeBook(){
//        ArrayList<Pair<Character,String>> result = new ArrayList<Pair<Character,String>>();
//        for(Map.Entry<Character, Node> entry : tree.map.entrySet()) {
//            Character c = entry.getKey();
//            String code = BoolListToString(tree.getCode(c));
//            result.add(new Pair<Character,String>(c,code));
//        }
//        Collections.sort(result,new Comparator<Pair<Character, String>>() {
//            @Override
//            public int compare(Pair<Character, String> o1, Pair<Character, String> o2) {
//                return o1.second.length()-o2.second.length();
//            }
//        });
//        return result;
//    }

    public String BoolListToString(List<Boolean> charCode){
        StringBuilder result = new StringBuilder(new String(""));
        for(Boolean b : charCode){
            String code = b ? "1" : "0";
            result.append(code);
        }
        return result.toString();
    }

    public int size(){
        if(tree != null && tree.root != null) return tree.root.getWeight();
        else return 0;
    }
}
