package eu.tutorials.kidsdrawingapp

import android.app.Dialog
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.widget.ConstraintSet
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.dialogue_brush_size.*


class DrawingView(context: Context, attrs:AttributeSet):View(context,attrs){

    private var mDrawPath : CustomPath? = null
    private var mCanvasBitmap: Bitmap? = null
    private var mDrawPaint : Paint? = null
    private var mCanvasPaint : Paint? = null
    private var mBrushSize : Float = 0.toFloat()
    private var color = Color.BLACK
    private var canvas: Canvas? = null
    private val mpaths = ArrayList<CustomPath>()
    private val mUndoPaths = ArrayList<CustomPath>()


    init{
        setupDrawing()
    }

    fun onClickUndo(){
        if(mpaths.size > 0){
            mUndoPaths.add(mpaths.removeAt(mpaths.size - 1))
            invalidate()
        }
    }
    private fun setupDrawing(){
        mDrawPaint = Paint()
        mDrawPath = CustomPath(color,mBrushSize)
        mDrawPaint!!.color = color
        mDrawPaint!!.style = Paint.Style.STROKE
        mDrawPaint!!.strokeJoin = Paint.Join.ROUND
        mDrawPaint!!.strokeCap = Paint.Cap.ROUND
        mCanvasPaint = Paint(Paint.DITHER_FLAG)
        //mBrushSize = 20.toFloat()

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mCanvasBitmap = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888)
        canvas  = Canvas(mCanvasBitmap!!)
    }
    //Change canvas to Canvas? if failed
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(mCanvasBitmap!!,0f,0f,mCanvasPaint)

        for(path in mpaths ){
            mDrawPaint!!.strokeWidth = path!!.brushThinckness
            mDrawPaint!!.color = path!!.color
            canvas.drawPath(path!!,mDrawPaint!!)
        }
        if(!mDrawPath!!.isEmpty){
            mDrawPaint!!.strokeWidth = mDrawPath!!.brushThinckness
            mDrawPaint!!.color = mDrawPath!!.color
            canvas.drawPath(mDrawPath!!,mDrawPaint!!)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        val touchX = event?.x
        val touchY = event?.y

        when(event?.action){
            MotionEvent.ACTION_DOWN->{
                mDrawPath!!.color = color
                mDrawPath!!.brushThinckness = mBrushSize

                mDrawPath!!.reset()
                mDrawPath!!.moveTo(touchX!!,touchY!!)
            }
            MotionEvent.ACTION_MOVE ->{
                mDrawPath!!.lineTo(touchX!!,touchY!!)
            }
            MotionEvent.ACTION_UP ->{
                mpaths.add(mDrawPath!!)
                mDrawPath = CustomPath(color,mBrushSize)
            }
            else->return false
        }
        invalidate()

        return true

    }

    fun setSizeForBrush(newSize : Float){
        mBrushSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,newSize,
                                        resources.displayMetrics)

        mDrawPaint!!.strokeWidth = mBrushSize


    }

    fun setColor(newColor : String){
        color = Color.parseColor(newColor)
        mDrawPaint!!.color = color
    }
    internal inner class CustomPath(var color : Int,var brushThinckness : Float) : Path(){


    }


}