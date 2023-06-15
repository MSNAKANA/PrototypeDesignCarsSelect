package com.example.prototypedesigncarsselect

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.prototypedesigncarsselect.databinding.ActivityCarsSelectSignUpBinding
import com.google.firebase.auth.FirebaseAuth

class CarsSelectSignUp : AppCompatActivity() {
    private lateinit var binding: ActivityCarsSelectSignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCarsSelectSignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.btnsignUp.setOnClickListener{
            val email = binding.etsEmailAddress.text.toString()
            val password = binding.pass.text.toString()
            val confirmPassword = binding.confirmPass.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()){
                if (password == confirmPassword){
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{
                        if (it.isSuccessful){
                            val intent = Intent(this, AddCategory::class.java)
                            startActivity(intent)
                            Toast.makeText(this, "Sign Up was successful", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Password does not matched", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
        binding.signupRedirectText.setOnClickListener {
            val loginIntent = Intent(this, Login2::class.java)
            startActivity(loginIntent)
        }
    }
}