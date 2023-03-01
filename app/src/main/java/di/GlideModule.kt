package di

import android.content.Context
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.load.model.*
import com.bumptech.glide.load.model.stream.BaseGlideUrlLoader
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.module.AppGlideModule
import com.jetpack.demo.R
import java.io.InputStream
import java.util.regex.Pattern


@GlideModule
class GlideUtils : AppGlideModule() {
    /**
     * Setting memory cache size 10M
     */
    private val cacheSize:Long = 10485760 //10*1024*1024
    override fun applyOptions(context: Context, builder: GlideBuilder) {
        builder.setMemoryCache(LruResourceCache(cacheSize))
    }
    /**
     * Register a BaseGlideUrlLoader of String type
     */
    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        registry.append(
            String::class.java, InputStream::class.java,
            CustomBaseGlideUrlLoader.factory
        )
    }

    /**
     * Close parsing Android Manifest
     */
    override fun isManifestParsingEnabled(): Boolean {
        return false
    }

    /**
     *
     */
    internal class CustomBaseGlideUrlLoader(
        concreteLoader: ModelLoader<GlideUrl, InputStream>,
        modelCache: ModelCache<String, GlideUrl>
    ): BaseGlideUrlLoader<String>(concreteLoader, modelCache){
        /**
         * Url The matching rules of 6550
         */
        private val pattern= Pattern.compile("__w-((?:-?\\d+)+)__")
        /**
         * Control the size of the image to be loaded
         */
        override fun getUrl(model: String, width: Int, height: Int, options: Options?): String {
            var  m=pattern.matcher(model)
            var bestBucket=0
            if (m.find()){
                var  found=m.group(1).split("-")
                for (item in found){
                    bestBucket=item.toInt()
                    if (bestBucket>=width) break
                }
            }
            return model
        }

        override fun handles(model: String): Boolean {
            return true
        }

        companion object{
            val urlCache= ModelCache<String, GlideUrl>(150)
            /**
             * The default is private and exposed to Java calls through the @JvmField annotation
             */
            @JvmField
            val factory=object: ModelLoaderFactory<String, InputStream> {
                override fun build(multiFactory: MultiModelLoaderFactory)
                        : ModelLoader<String, InputStream> {

                    return CustomBaseGlideUrlLoader(
                        multiFactory.build(GlideUrl::class.java, InputStream::class.java), urlCache
                    )
                }
                override fun teardown() {
                }
            }
        }
    }

    fun loadImage(context: Context, url: String?, imageView: AppCompatImageView) {

        GlideApp.with(context)
            .load(url)
            .transform(CenterCrop(),RoundedCorners(context.resources.getInteger(R.integer.rounded_image_radius)))
            .transition(DrawableTransitionOptions.withCrossFade()).into(imageView)

    }
}

