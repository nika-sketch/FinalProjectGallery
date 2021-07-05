package ge.nlatsabidze.galleryapp.ui.gallery

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import ge.nlatsabidze.galleryapp.R
import java.util.*
import kotlin.collections.ArrayList


class GalleryFragment : Fragment() {

    private val IMAGE_REQUEST_CODE = 155

    private var mAuth: FirebaseAuth? = null

    private var addImageBtn: FloatingActionButton? = null

    private var imagesRecyclerViewRefresher: SwipeRefreshLayout? = null

    private var imagesRecyclerView: RecyclerView? = null

    private var imagesRecyclerViewAdapter: GalleryImagesAdapter? = null

    private var firebaseStore: FirebaseStorage? = null

    private var storageReference: StorageReference? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_gallery, container, false)

        addImageBtn = root.findViewById<FloatingActionButton>(R.id.addImageBtn)

        imagesRecyclerViewRefresher = root.findViewById(R.id.imagesRecyclerViewRefresher)
        imagesRecyclerViewRefresher!!.setOnRefreshListener {
            updateImagesStore()
        }

        imagesRecyclerViewAdapter = GalleryImagesAdapter(ArrayList<String>())
        imagesRecyclerView = root.findViewById(R.id.imagesRecyclerView)
        imagesRecyclerView!!.adapter = imagesRecyclerViewAdapter
        imagesRecyclerView!!.layoutManager = GridLayoutManager(context, 2)

        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference

        mAuth = FirebaseAuth.getInstance()
        mAuth!!.addAuthStateListener { state ->
            run {
                val currentUser = state.currentUser
                if (currentUser != null) {
                    addImageBtn!!.visibility = View.VISIBLE
                } else {
                    addImageBtn!!.visibility = View.INVISIBLE
                }
            }
        }

        addImageBtn!!.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 2000)
            } else {
                openGallery()
            }
        }

        updateImagesStore()
        return root
    }

    private fun updateImagesStore() {
        imagesRecyclerViewAdapter!!.clearItems();
        storageReference!!.child("images/").listAll().addOnSuccessListener { result ->
            run {
                val imageUrls: ArrayList<String> = ArrayList<String>()
                for (fileRef: StorageReference in result.items) {
                    fileRef.downloadUrl.addOnSuccessListener { uri ->
                        imagesRecyclerViewAdapter!!.addImage(uri.toString())
                    }
                }
            }
        }
        imagesRecyclerViewRefresher!!.isRefreshing = false;
    }

    private fun openGallery() {
        val gallery = Intent(Intent.ACTION_GET_CONTENT)
        gallery.type = "image/*"
        startActivityForResult(Intent.createChooser(gallery, "Select a Picture:"), IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && requestCode == IMAGE_REQUEST_CODE) {
            val imageUri = data?.data
            uploadImage(imageUri);
        }
    }

    private fun uploadImage(imageUri: Uri?) {
        val ref = storageReference?.child("images/" + UUID.randomUUID().toString())
        ref?.putFile(imageUri!!)!!.addOnSuccessListener {
            updateImagesStore()
        }
    }
}