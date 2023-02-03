import org.gradle.api.GradleException
import java.security.MessageDigest
import java.util.*
import java.util.concurrent.atomic.AtomicReference
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

fun sha256(password: String): ByteArray {
    val bytes = password.toByteArray()
    val md = MessageDigest.getInstance("SHA-256")
    return md.digest(bytes)
}

val algorithm = "AES/CBC/PKCS5Padding"
val iv = IvParameterSpec("0".repeat(16).toByteArray())

fun AES256Decrypt(cipherText: String, password: String): String {
    val key = SecretKeySpec(sha256(password), "AES")
    val cipher = Cipher.getInstance(algorithm)
    cipher.init(Cipher.DECRYPT_MODE, key, iv)
    val plainText = cipher.doFinal(Base64.getDecoder().decode(cipherText))
    return String(plainText)
}

fun AES256Encrypt(inputText: String, password: String): String {
    val key = SecretKeySpec(sha256(password), "AES")
    val cipher = Cipher.getInstance(algorithm)
    cipher.init(Cipher.ENCRYPT_MODE, key, iv)
    val cipherText = cipher.doFinal(inputText.toByteArray())
    return Base64.getEncoder().encodeToString(cipherText)
}

val inputText = "abcdefghigklmnopqrstuvwxyz0123456789"
val AES_KEY_ARGUMENT = "aes-key"

val _group = "io.github.zbvs"
val _version = "1.0.4"
val _artifact = "nexus-deploy-test"


val aesKeyPhrase = AtomicReference<String>()
