package kr.hs.emirim.neon;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import static android.R.id.message;

/**
 * Created by 내컴퓨터 on 2016-10-31.
 */

public class WorkAcitivity extends AppCompatActivity {

    Button write,font,color;
    ImageView pho;
    EditText text;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work);
        pho = (ImageView) findViewById(R.id.back);
        write=(Button)findViewById(R.id.btn_write);
        font=(Button)findViewById(R.id.btn_font);
        color=(Button)findViewById(R.id.btn_chcolor);
        text=(EditText)findViewById(R.id.edit_write);
        if(S.check==1)doTakePhotoAction();
        else if(S.check==2)doTakeAlbumAction();

        write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CharSequence[] items = { "빨간색", "노란색", "파란색", "귤색" };

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getLayoutInflater().getContext());
                alertDialogBuilder.setTitle("글자 색 변경");
                alertDialogBuilder.setItems(items,new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id) {

                        // 프로그램을 종료한다
                        Toast.makeText(getApplicationContext(),
                                items[id] + " 선택했습니다.",
                                Toast.LENGTH_SHORT).show();
                        if(id==0)
                        {
                            text.setTextColor(Color.RED);
                        }else if(id==1)
                        {
                            text.setTextColor(Color.YELLOW);
                        }else if(id==2)
                        {
                            text.setTextColor(Color.BLUE);

                        }else if(id==3)
                        {
                            text.setTextColor(Color.parseColor("#F3F532"));

                        }
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog1 = alertDialogBuilder.create();

                alertDialog1.show();
            }
        });
        font.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final CharSequence[] items = { "나눔 고딕", "아리따 돋움", "맑은 고딕", "굴림" };

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getLayoutInflater().getContext());
                alertDialogBuilder.setTitle("폰트 선택");
                alertDialogBuilder.setItems(items,new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id) {

                                // 프로그램을 종료한다
                                Toast.makeText(getApplicationContext(),
                                        items[id] + " 선택했습니다.",
                                        Toast.LENGTH_SHORT).show();
                                if(id==0)
                                {
                                    Typeface typeFace = Typeface.createFromAsset(getAssets(), "aGodic.ttf");
                                    text.setTypeface(typeFace);
                                }else if(id==1)
                                {
                                    Typeface typeFace = Typeface.createFromAsset(getAssets(), "arita.ttf");
                                    text.setTypeface(typeFace);
                                }else if(id==2)
                                {
                                    Typeface typeFace = Typeface.createFromAsset(getAssets(), "malgun.ttf");
                                    text.setTypeface(typeFace);
                                }else if(id==3)
                                {
                                    Typeface typeFace = Typeface.createFromAsset(getAssets(), "gulim.ttf");
                                    text.setTypeface(typeFace);
                                }
                                dialog.dismiss();
                            }
                        });
                AlertDialog alertDialog1 = alertDialogBuilder.create();

                alertDialog1.show();

            }
        });

    }
    public void doTakePhotoAction() // 카메라 촬영 후 이미지 가져오기
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // 임시로 사용할 파일의 경로를 생성
        String url = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
        S.mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));

        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, S.mImageCaptureUri);
        startActivityForResult(intent, S.PICK_FROM_CAMERA);
    }

    /**
     * 앨범에서 이미지 가져오기
     */
    public void doTakeAlbumAction() // 앨범에서 이미지 가져오기
    {
        // 앨범 호출
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, S.PICK_FROM_ALBUM);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        if(resultCode != RESULT_OK)
            return;

        switch(requestCode) {
            case S.PICK_FROM_ALBUM: {
                // 이후의 처리가 카메라와 같으므로 일단  break없이 진행합니다.
                // 실제 코드에서는 좀더 합리적인 방법을 선택하시기 바랍니다.
                S.mImageCaptureUri = data.getData();
                Log.d("SmartWheel", S.mImageCaptureUri.getPath().toString());
            }

            case S.PICK_FROM_CAMERA: {
                // 이미지를 가져온 이후의 리사이즈할 이미지 크기를 결정합니다.
                // 이후에 이미지 크롭 어플리케이션을 호출하게 됩니다.
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(S.mImageCaptureUri, "image/*");

                // CROP할 이미지를 500*500 크기로 저장
                intent.putExtra("outputX", 500); // CROP한 이미지의 x축 크기
                intent.putExtra("outputY", 500); // CROP한 이미지의 y축 크기
                intent.putExtra("aspectX", 1); // CROP 박스의 X축 비율
                intent.putExtra("aspectY", 1); // CROP 박스의 Y축 비율
                intent.putExtra("scale", true);
                intent.putExtra("return-data", true);
                startActivityForResult(intent, S.CROP_FROM_iMAGE); // CROP_FROM_CAMERA case문 이동
                break;
            }
            case S.CROP_FROM_iMAGE: {
                // 크
                //
                // 롭이 된 이후의 이미지를 넘겨 받습니다.
                // 이미지뷰에 이미지를 보여준다거나 부가적인 작업 이후에
                // 임시 파일을 삭제합니다.
                if (resultCode != RESULT_OK) {
                    return;
                }

                final Bundle extras = data.getExtras();

                // CROP된 이미지를 저장하기 위한 FILE 경로
                String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() +
                        "/SmartWheel/" + System.currentTimeMillis() + ".jpg";

                if (extras != null) {
                    Bitmap photo = extras.getParcelable("data"); // CROP된 BITMAP
                    pho.setImageBitmap(photo); // 레이아웃의 이미지칸에 CROP된 BITMAP을 보여줌

                    storeCropImage(photo, filePath); // CROP된 이미지를 외부저장소, 앨범에 저장한다.
                    S.absoultePath = filePath;
                    break;

                }
                // 임시 파일 삭제
                File f = new File(S.mImageCaptureUri.getPath());
                if (f.exists()) {
                    f.delete();
                }
            }
        }}
    private void storeCropImage(Bitmap bitmap, String filePath) {
        // SmartWheel 폴더를 생성하여 이미지를 저장하는 방식이다.
        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/SmartWheel";
        File directory_SmartWheel = new File(dirPath);

        if (!directory_SmartWheel.exists()) // SmartWheel 디렉터리에 폴더가 없다면 (새로 이미지를 저장할 경우에 속한다.)
            directory_SmartWheel.mkdir();

        File copyFile = new File(filePath);
        BufferedOutputStream out = null;

        try {

            copyFile.createNewFile();
            out = new BufferedOutputStream(new FileOutputStream(copyFile));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 500, out);

            // sendBroadcast를 통해 Crop된 사진을 앨범에 보이도록 갱신한다.
            sendBroadcast(new Intent(android.content.Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                    Uri.fromFile(copyFile)));

            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



