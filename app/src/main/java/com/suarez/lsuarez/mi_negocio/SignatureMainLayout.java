package com.suarez.lsuarez.mi_negocio;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

public class SignatureMainLayout extends LinearLayout implements OnClickListener {

    LinearLayout buttonsLayout;
    SignatureView signatureView;
    private OnItemClickListener listener;

    public SignatureMainLayout(Context context, OnItemClickListener listener) {
        super(context);

        this.listener = listener;
        this.setOrientation(LinearLayout.VERTICAL);
        this.buttonsLayout = this.buttonsLayout();
        this.signatureView = new SignatureView(context);

        // add the buttons and signature views
        this.addView(this.buttonsLayout);
        this.addView(signatureView);

    }

    private LinearLayout buttonsLayout() {

        // Crear GUI
        LinearLayout linearLayout = new LinearLayout(this.getContext());
        ImageButton saveBtn   = new ImageButton(this.getContext());
        ImageButton clearBtn  = new ImageButton(this.getContext());
        ImageButton cancelBtn = new ImageButton(this.getContext());

        // Orientación
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setBackgroundColor(Color.parseColor("#14121d"));

        // Boton Guardar
        saveBtn.setTag("Guardar");
        saveBtn.setOnClickListener(this);
        saveBtn.setImageDrawable(getResources().getDrawable(R.drawable.ok_mini));
        saveBtn.setId(R.id.guardaID);
        saveBtn.setBackground(Drawable.createFromPath("@null"));

        // Boton Borrar
        clearBtn.setTag("Borrar");
        clearBtn.setOnClickListener(this);
        clearBtn.setImageDrawable(getResources().getDrawable(R.drawable.renew_mini));
        clearBtn.setBackground(Drawable.createFromPath("@null"));

        // Boton Cerrar
        cancelBtn.setTag("Cerrar");
        cancelBtn.setOnClickListener(this);
        cancelBtn.setId(R.id.cerrarID);
        cancelBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_cancel));
        cancelBtn.setBackground(Drawable.createFromPath("@null"));


        linearLayout.addView(saveBtn);
        linearLayout.addView(clearBtn);
        linearLayout.addView(cancelBtn);

        // return layout
        return linearLayout;
    }

    // the on click listener of 'save' and 'clear' buttons
    @Override
    public void onClick(View v) {
        String tag = v.getTag().toString().trim();

        // Guardar
        if (tag.equalsIgnoreCase("guardar")) {
            this.saveImage(this.signatureView.getSignature());
            listener.onGuardarClick();
        }

        // Borrar
        if (tag.equalsIgnoreCase("borrar")) {
            this.signatureView.clearSignature();
        }

    }

    /**
     * save the signature to an sd card directory
     * @param signature bitmap
     */
    final void saveImage(Bitmap signature) {

        String root = Environment.getExternalStorageDirectory().toString();

        // Directorio donde se guardará
        File myDir = new File(root + "/saved_signature");

        // make the directory if it does not exist yet
        if (!myDir.exists()) {
            myDir.mkdirs();
        }

        // Nombre de imagen
        String fname = "firma_postulante.png";

        // Si existe elimino
        File file = new File(myDir, fname);
        if (file.exists()) {

        }

        try {

            // Guardar en carpeta
            FileOutputStream out = new FileOutputStream(file);
            signature.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();

            Toast.makeText(this.getContext(), "Guardando firma..", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * The View where the signature will be drawn
     */
    private class SignatureView extends View {

        // set the stroke width
        private static final float STROKE_WIDTH = 5f;
        private static final float HALF_STROKE_WIDTH = STROKE_WIDTH / 2;

        private Paint paint = new Paint();
        private Path path = new Path();

        private float lastTouchX;
        private float lastTouchY;
        private final RectF dirtyRect = new RectF();

        public SignatureView(Context context) {

            super(context);

            paint.setAntiAlias(true);
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeWidth(STROKE_WIDTH);

            // Color de fondo blanco
            this.setBackgroundColor(Color.WHITE);

            // width and height should cover the screen
            this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 600));

        }

        /**
         * Get signature
         *
         * @return
         */
        protected Bitmap getSignature() {

            Bitmap signatureBitmap = null;

            // set the signature bitmap
            if (signatureBitmap == null) {
                signatureBitmap = Bitmap.createBitmap(this.getWidth(), this.getHeight(), Bitmap.Config.RGB_565);
            }

            // important for saving signature
            final Canvas canvas = new Canvas(signatureBitmap);
            this.draw(canvas);

            return signatureBitmap;
        }

        /**
         * clear signature canvas
         */
        private void clearSignature() {
            path.reset();
            this.invalidate();
        }

        // all touch events during the drawing
        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawPath(this.path, this.paint);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float eventX = event.getX();
            float eventY = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:

                    path.moveTo(eventX, eventY);

                    lastTouchX = eventX;
                    lastTouchY = eventY;
                    return true;

                case MotionEvent.ACTION_MOVE:

                case MotionEvent.ACTION_UP:

                    resetDirtyRect(eventX, eventY);
                    int historySize = event.getHistorySize();
                    for (int i = 0; i < historySize; i++) {
                        float historicalX = event.getHistoricalX(i);
                        float historicalY = event.getHistoricalY(i);

                        expandDirtyRect(historicalX, historicalY);
                        path.lineTo(historicalX, historicalY);
                    }
                    path.lineTo(eventX, eventY);
                    break;

                default:

                    return false;
            }

            invalidate((int) (dirtyRect.left - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.top - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.right + HALF_STROKE_WIDTH),
                    (int) (dirtyRect.bottom + HALF_STROKE_WIDTH));

            lastTouchX = eventX;
            lastTouchY = eventY;

            return true;
        }

        private void expandDirtyRect(float historicalX, float historicalY) {
            if (historicalX < dirtyRect.left) {
                dirtyRect.left = historicalX;
            } else if (historicalX > dirtyRect.right) {
                dirtyRect.right = historicalX;
            }

            if (historicalY < dirtyRect.top) {
                dirtyRect.top = historicalY;
            } else if (historicalY > dirtyRect.bottom) {
                dirtyRect.bottom = historicalY;
            }
        }

        private void resetDirtyRect(float eventX, float eventY) {
            dirtyRect.left = Math.min(lastTouchX, eventX);
            dirtyRect.right = Math.max(lastTouchX, eventX);
            dirtyRect.top = Math.min(lastTouchY, eventY);
            dirtyRect.bottom = Math.max(lastTouchY, eventY);
        }

    }

}