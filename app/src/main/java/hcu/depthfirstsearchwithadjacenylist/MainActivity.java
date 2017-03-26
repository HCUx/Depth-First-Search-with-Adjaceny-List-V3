package hcu.depthfirstsearchwithadjacenylist;

import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
    private LinearLayout linearNodeDegree;
    private TextView nodeDegreeBack,totalEdgeCountBack,completeGraphBack,accessTimeBack,processTimeBack,ExplorerBack;
    private String MatrixFileRow;
    private String[] MatrixFileRowParts;
    private Node[] node;
    private int nodeCount = 0,totalEdge = 0;
    private Watch ZamanTutucu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ana_ekran);//ana ekran tasarımını set ettik.
        initUI(); //ekrandaki componentleri yüklemesi için fonksiyonu çağırdık
        ReadNodeCountFromFile(); //düğüm sayısını buluyoruz
        CreateFirstNodes(); //ilk düğümleri oluşturuyoruz
        ReadMatrixFromFile_CreateGraph(); //Dosyadan komşuluk değerlerini alıp düğümleri bağlıyoruz
        DFS_Gezgini(node[0]); //DFS'e 0.nodu gönderiyoruz
        setListeners(); //butona tıklama dinleyicisi yüklemek için gerekli methoda haber veriyoruz
        copleteGraph_and_EdgeCount();
        //DTime_and_FTime();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    protected void initUI(){
        dereceGetir = (Button) findViewById(R.id.dereceButton); //xml den id'ye göre componentleri buluyoruz.
        dereceText = (EditText) findViewById(R.id.dereceTextField);
        linearNodeDegree = (LinearLayout) findViewById(R.id.LinearNodeDegree);
        nodeDegreeBack = (TextView) findViewById(R.id.nodeDegreeBack);
        totalEdgeCountBack = (TextView) findViewById(R.id.totalEdgeCountBack);
        completeGraphBack = (TextView) findViewById(R.id.completeGraphBack);
        accessTimeBack = (TextView) findViewById(R.id.accessTimeBack);
        processTimeBack = (TextView) findViewById(R.id.processTimeBack);
        ExplorerBack = (TextView) findViewById(R.id.explorerBack);

        nodeDegreeBack.setTextColor(Color.MAGENTA); //Kullanının sorduğu düğüm derecesi rengi
        linearNodeDegree.setVisibility(View.GONE);  //Kullanıcıdan değer alınmadan önce gösterilmesini istemiyoruz.
    }

    protected void ReadNodeCountFromFile(){
        File fileMatrix = new File(Environment.getExternalStorageDirectory(),"DFSMatrix.txt");//Dosyayı açıyoruz.
        try {
            //bu kısımda toplam düğüm sayısını buluyoruz
            BufferedReader FileMatrixBufferR = new BufferedReader(new FileReader(fileMatrix)); //dosyayı okuyacak bellek alanı için yardımcı sınıfı çağırdık
            MatrixFileRow = FileMatrixBufferR.readLine();//dosyamızın ilk satırını okuduk
            MatrixFileRowParts = MatrixFileRow.split(" "); //okunana satırdaki elemanları diziye attık
            nodeCount = MatrixFileRowParts.length; //dizi eleman sayısı bizim düğüm sayımız
            FileMatrixBufferR.close();
            MatrixFileRowParts = null;
            MatrixFileRow = null;
        }catch (FileNotFoundException e){
            Toast.makeText(getApplicationContext(),"DOSYA BULUNAMADI",Toast.LENGTH_LONG).show();
        }
        catch (IOException e){
            Toast.makeText(getApplicationContext(),"DOSYA OKUNAMADI",Toast.LENGTH_LONG).show();
        }
    }

    protected void CreateFirstNodes(){
        node = new Node[nodeCount];
        for(int i=0;i<nodeCount;i++){
            node[i] = new Node(nodeCount);
            node[i].setNodeName(i+" Nolu Node");
        }
    }

    protected void ReadMatrixFromFile_CreateGraph(){
        File fileMatrix = new File(Environment.getExternalStorageDirectory(),"DFSMatrix.txt");//Dosyayı açıyoruz.
        try {
            //bu kısımda toplam düğüm sayısını buluyoruz
            BufferedReader FileMatrixBufferR = new BufferedReader(new FileReader(fileMatrix)); //dosyayı okuyacak bellek alanı için yardımcı sınıfı çağırdık
            int arrayIndex=0;
            MatrixFileRow = FileMatrixBufferR.readLine();//dosyamızın ilk satırını okuduk
            while(MatrixFileRow!=null) {
                MatrixFileRowParts = MatrixFileRow.split(" "); //okunana satırdaki elemanları diziye attık
                node[arrayIndex].setAdjacent(node,MatrixFileRowParts);
                MatrixFileRow = FileMatrixBufferR.readLine();
                arrayIndex++;
            }
        }catch (FileNotFoundException e){
            Toast.makeText(getApplicationContext(),"DOSYA BULUNAMADI",Toast.LENGTH_LONG).show();
        }
        catch (IOException e){
            Toast.makeText(getApplicationContext(),"DOSYA OKUNAMADI",Toast.LENGTH_LONG).show();
        }
    }

    //DFS ALGORİTMASINA GÖRE GEZGİN
    protected void DFS_Gezgini(Node node){
        node.setNode_DTime(node.getNode_DTime()+1);
        ExplorerBack.setText(ExplorerBack.getText()+node.getNodeName()+" Girildi\n");

        if(node.getNodeColor().equalsIgnoreCase("White")){

            node.setNodeColor("Grey");
            ZamanTutucu = new Watch();
            ZamanTutucu.startAccessTime();
            accessTimeBack.setText(accessTimeBack.getText()+"[D]"+node.getNodeName()+" Erişildi\n");
            ExplorerBack.setText(ExplorerBack.getText()+node.getNodeName()+" Gri Yapıldı\n");

            for(int i = 0 ; i<node.getAdjacentCount() ; i++){
                if(node.getAdjacent()[i] != null && node.getAdjacent()[i].getNodeColor().equalsIgnoreCase("White")){
                    DFS_Gezgini(node.getAdjacent()[i]);
                }
            }

        }

        node.setNodeColor("Black");
        ExplorerBack.setText(ExplorerBack.getText()+node.getNodeName()+" Siyah Yapıldı\n");
        ZamanTutucu.startProcessTime();
        accessTimeBack.setText(accessTimeBack.getText()+"[F]"+node.getNodeName()+" İşlendi\n");
        node.setNode_FTime(node.getNode_FTime()+1);
        ExplorerBack.setText(ExplorerBack.getText()+node.getNodeName()+" Çıkıldı\n");

    }

    protected void setListeners(){
        dereceGetir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //butona fonksiyonu buradan atıyoruz
                nodeDegreeBack.setText(String.valueOf(node[Integer.valueOf(dereceText.getText().toString())].getNodeDegree()));
                linearNodeDegree.setVisibility(View.VISIBLE);
            }
        });
    }

    //Complete Graph mı ve Kenar Sayısı Tespiti
    protected void copleteGraph_and_EdgeCount(){
        for(int i = 0;i<nodeCount;i++){
            totalEdge += node[i].getNodeDegree();
        }
        totalEdge /= 2;
        totalEdgeCountBack.setText(totalEdge+"");

        if(totalEdge != nodeCount*(nodeCount-1)/2)
            completeGraphBack.setText("HAYIR");
        else
            completeGraphBack.setText("EVET");

    }

    protected void DTime_and_FTime(){
        for(int i = 0;i<node.length;i++){
            accessTimeBack.setText(accessTimeBack.getText()+""+node[i].getNodeName()+" için "+node[i].getNode_DTime()+"\n");
            processTimeBack.setText(processTimeBack.getText()+""+node[i].getNodeName()+" için "+node[i].getNode_DTime()+"\n");
        }
    }
}
