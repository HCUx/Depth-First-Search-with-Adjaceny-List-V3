package hcu.depthfirstsearchwithadjacenylist;

import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private Button dereceGetir;
    private EditText dereceText;
    private LinearLayout linearNodeDegree,InfoLayout;
    ListView AccessTimeList,ExplorerList;
    private TextView nodeDegreeBack,totalEdgeCountBack,completeGraphBack;
    private String MatrixFileRow;
    private String[] MatrixFileRowParts;
    private Node[] node;
    private int nodeCount = 0,totalEdge = 0;
    ArrayAdapter<String> ExplorerArray,AccessArray;
    Adjacent ilk=null,gecici=null,son=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ana_ekran);//ana ekran tasarımını set ettik.
        initUI(); //ekrandaki componentleri yüklemesi için fonksiyonu çağırdık
        boolean fileState = ReadNodeCountFromFile(); //node sayısı tespiti için fonksiyon
        InfoLayout.setVisibility(View.GONE); //dosya okunamadı ise componentleri unvisible yapıyoruz

        if(fileState){
            InfoLayout.setVisibility(View.VISIBLE); //dosya okundu componentler visible oldu
            CreateFirstNodes(); //ilk düğümleri oluşturuyoruz
            ReadMatrixFromFile_CreateGraph(); //Dosyadan komşuluk değerlerini alıp düğümleri bağlıyoruz
            Node_Call(); //Nodeları DFS E Göre Gezmek için gerekli fonksiyonun başlangıcı
            setListeners(); //butona tıklama dinleyicisi yüklemek için gerekli methoda haber veriyoruz
            completeGraph_and_EdgeCount(); //Complete Graf ve Kenar Sayısı Bulma
        }else
            Toast.makeText(MainActivity.this,"DFSMatrix.Txt Dosyası Oluştur",Toast.LENGTH_LONG).show();

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    protected void initUI(){
        dereceGetir = (Button) findViewById(R.id.dereceButton); //xml den id'ye göre componentleri buluyoruz.

        dereceText = (EditText) findViewById(R.id.dereceTextField);

        linearNodeDegree = (LinearLayout) findViewById(R.id.LinearNodeDegree);
        InfoLayout = (LinearLayout) findViewById(R.id.InfoLayoutAll);

        nodeDegreeBack = (TextView) findViewById(R.id.nodeDegreeBack);
        totalEdgeCountBack = (TextView) findViewById(R.id.totalEdgeCountBack);
        completeGraphBack = (TextView) findViewById(R.id.completeGraphBack);

        ExplorerList = (ListView) findViewById(R.id.ExplorerBack);
        AccessTimeList = (ListView) findViewById(R.id.accessTimeBack);

        AccessArray = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
        ExplorerArray = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
        ExplorerList.setAdapter(ExplorerArray);
        AccessTimeList.setAdapter(AccessArray);

        nodeDegreeBack.setTextColor(Color.MAGENTA); //Kullanının sorduğu düğüm derecesi rengi
        linearNodeDegree.setVisibility(View.GONE);  //Kullanıcıdan değer alınmadan önce gösterilmesini istemiyoruz.
    }

    protected boolean ReadNodeCountFromFile(){
        File fileMatrix = new File(Environment.getExternalStorageDirectory(),"DFSMatrix.txt");//Dosyayı açıyoruz.

        try {
            //bu kısımda toplam düğüm sayısını buluyoruz
            BufferedReader FileMatrixBufferR = new BufferedReader(new FileReader(fileMatrix)); //dosyayı okuyacak bellek alanı için yardımcı sınıfı çağırdık
            MatrixFileRow = FileMatrixBufferR.readLine();//dosyamızın ilk satırını okuduk
            MatrixFileRowParts = MatrixFileRow.split(" "); //okunana satırdaki elemanları diziye attık
            nodeCount = MatrixFileRowParts.length; //dizi eleman sayısı bizim düğüm sayımız
            FileMatrixBufferR.close(); //dosyayı kapattık
            MatrixFileRowParts = null; //bufferları null yaptık ileride kullanırken sıkıntı çıkmasın
            MatrixFileRow = null;
            return true; //dosya bulunduğı için true döndürdük

        }catch (FileNotFoundException e){
            Toast.makeText(getApplicationContext(),"DOSYA BULUNAMADI",Toast.LENGTH_LONG).show();
            return false;
        }
        catch (IOException e){
            Toast.makeText(getApplicationContext(),"DOSYA OKUNAMADI",Toast.LENGTH_LONG).show();
            return false;
        }

    }

    protected void CreateFirstNodes(){

        node = new Node[nodeCount];

        for(int i=0;i<nodeCount;i++){
            node[i] = new Node();
            node[i].setNodeName(i+" Nolu Node");
        }

    }

    //Dosyadan Matrisi Okuyup Bağlı Liste Mantığı ile komşuluk listesi oluşturduk.
    protected void ReadMatrixFromFile_CreateGraph(){

        File fileMatrix = new File(Environment.getExternalStorageDirectory(),"DFSMatrix.txt");//Dosyayı açıyoruz.

        try {

            BufferedReader FileMatrixBufferR = new BufferedReader(new FileReader(fileMatrix)); //dosyayı okuyacak bellek alanı için yardımcı sınıfı çağırdık
            MatrixFileRow = FileMatrixBufferR.readLine();//dosyamızın ilk satırını okuduk
            int nodeIndex=0;//matrisin kaçıncı satırını okudumuzun sayacı
            while(MatrixFileRow!=null) { //matris bitene kadar

                MatrixFileRowParts = MatrixFileRow.split(" "); //okunana satırdaki elemanları diziye attık

                for(int i=0;i<MatrixFileRowParts.length;i++){

                    if (MatrixFileRowParts[i].equalsIgnoreCase("1")){ //matristen okunan satırda 1 olan elemanlar ile bağlı liste yapacağız

                        if(ilk==null){
                            ilk=new Adjacent();
                            node[nodeIndex].setAdjacent(ilk);
                            ilk.setNodeReference(node[i]);
                            ilk.setNext(null);
                            son=ilk;
                        }else{
                            gecici = new Adjacent();
                            gecici.setNext(null);
                            gecici.setNodeReference(node[i]);
                            son.setNext(gecici);
                            son=gecici;
                        }

                    }

                }
                //sonraki node için null yapıyoruz
                ilk=null;
                gecici=null;
                son=null;

                MatrixFileRow = FileMatrixBufferR.readLine();
                nodeIndex++;

            }

        }catch (FileNotFoundException e){
            Toast.makeText(getApplicationContext(),"DOSYA BULUNAMADI",Toast.LENGTH_LONG).show();
        }
        catch (IOException e){
            Toast.makeText(getApplicationContext(),"DOSYA OKUNAMADI",Toast.LENGTH_LONG).show();
        }

    }

    protected void Node_Call(){
        for (Node aNode : node) {
            if (aNode.getNodeColor().equalsIgnoreCase("White")) {
                DFS_Gezgini(aNode);
            }
        }
    }

    //DFS ALGORİTMASINA GÖRE GEZGİN
    protected void DFS_Gezgini(Node node){

        node.setNodeColor("Gray");
        node.setDTime(node.getDTime()+1);

        ExplorerArray.add(node.getNodeName()+" Gri Yapıldı\n");
        ExplorerArray.notifyDataSetChanged();

        Adjacent adjacent = node.getAdjacent();
        Node nd;

        while(adjacent != null){
            nd = adjacent.getNodeReference();
            if(nd.getNodeColor().equalsIgnoreCase("White")){
                DFS_Gezgini(nd);
            }
            adjacent = adjacent.getNext();
        }
        node.setFtime(node.getFtime()+1);
        node.setNodeColor("Black");

        ExplorerArray.add(node.getNodeName()+" Siyah\n");
        ExplorerArray.notifyDataSetChanged();

        AccessArray.add("[D]"+node.getNodeName()+" "+node.getDTime());
        AccessArray.add("[F]"+node.getNodeName()+" "+node.getFtime());
        AccessArray.notifyDataSetChanged();
        /*

        AccessArray.add("[D]"+node.getNodeName()+" Erişildi\n");
        AccessArray.notifyDataSetChanged();
*/
    }


    protected void setListeners(){

        dereceGetir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //butona fonksiyonu buradan atıyoruz
                int NodeNumber = Integer.valueOf(dereceText.getText().toString());
                int NodeDegree = 0;
                Adjacent adjacents = node[NodeNumber].getAdjacent();

                while(adjacents != null){
                    NodeDegree++;
                    adjacents = adjacents.getNext();
                }

                nodeDegreeBack.setText(String.valueOf(NodeDegree));
                linearNodeDegree.setVisibility(View.VISIBLE);
            }
        });

    }

    //Complete Graph mı ve Kenar Sayısı Tespiti
    protected void completeGraph_and_EdgeCount(){

        int NodeDegree=0;
        for(int i = 0;i<nodeCount;i++){
            Adjacent adjacents = node[i].getAdjacent();

            while(adjacents != null){
                NodeDegree++;
                adjacents = adjacents.getNext();
            }

            totalEdge += NodeDegree;
            NodeDegree=0;
        }
        totalEdge /= 2;
        totalEdgeCountBack.setText(String.valueOf(totalEdge));

        if(totalEdge != nodeCount*(nodeCount-1)/2)
            completeGraphBack.setText("HAYIR");
        else
            completeGraphBack.setText("EVET");

    }

}
