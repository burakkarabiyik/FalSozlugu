package tr.mvr.burak.fal;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.io.IOException;
import android.os.NetworkOnMainThreadException;
import android.os.StrictMode;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import static org.jsoup.Jsoup.connect;


public class MainActivity extends AppCompatActivity {
    String liste="";
    String baslik;
    String site="http://www.kahvefalinda.com";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn = (Button) findViewById(R.id.button);

         final TextView tv1 = (TextView) findViewById(R.id.tv1);//TextView tanımladık
          final  EditText edt=(EditText)findViewById(R.id.et1);//EditText tanımaldık
        btn.setOnClickListener(new View.OnClickListener() {//Butona onclick eventi ekledik
            @Override
            public void onClick(View v) {
                //Yapılacaklar
                if(edt.getText().toString().trim().equals(""))
                //Girilen değerin boşlukları silinip boşmu kontrol etttik
                {
                    //Eğer Boşsa Falın sonucu gösterileceği yere bunu yazacak
                    tv1.setText("Aranılacak Kelimeyi Giriniz ");
                }
                else
                {
                    //Girilen değeri site adresine ekliyoruz aramayı siteye göre yapıcaz
                    //Mesela site.com/girilecekdeger gibi olacak
                    site = site + "/"+edt.getText();
                    new MyTask().execute();//Buda fonksiyonumuzu çalıştıracak
                }
            }
        });

    }


    private class MyTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {

            try {
                Document web; //Döküman oluşturduk birtane
                web=Jsoup.connect(site).timeout(0).get();//Dökümana siteye bağlanıp HTML kodlarını çektik hepsi geldi
                org.jsoup.select.Elements tags=web.getElementsByClass("entry").select("p");
                //Soup ile önce entry ismindeki classları çektik sonra bunlardan p paragraf olanları seçtik
                liste="";//Bir liste oluşturduk bu liste bizim fal sonuçları olacak ekrana basacaz
                for(int i=0;i<tags.size();i++)//Tags.size dediğimiz bizim siteden çektiklerimiz satır satır olacak
                {
                    liste=liste+tags.get(i).text();
                    // burada listeye çektiğimiz verilerin text kısımlarını satır satır ekletiyorum
                        //text istemekten başka daha fonksiyonları da var onlarıda incele
                    //jsoup baya iyi birşey :)
                }
            } catch (IOException e) {
                Log.e("Err", "Err var" + e.getMessage());
            }
            return liste;
        }


        @Override
        protected void onPostExecute(String result) {
            //if you had a ui element, you could display the title
            if(liste.equals(""))
            {//Liste boş ise yani aranadan değer anlamsız veya bilinmeyen birşey girerse
                //Boş dönecektir boş dönerse bulunamadı yazacak ekrana
                ((TextView)findViewById (R.id.tv1)).setText ("Bulunamadı");

            }
            else//Boş değilse listeyi yazdıracak
            ((TextView)findViewById (R.id.tv1)).setText (liste);
            ((TextView)findViewById(R.id.baslik)).setText(baslik);
            site="http://www.kahvefalinda.com";//Burada ise siteyi tekrar eski haline getiriyoruz
            //sitenin sonunda / işareti bulunamazmış yani http://www.google.com/ gibi olmazmış kabul etmiyor kendileri.
        }
    }

}