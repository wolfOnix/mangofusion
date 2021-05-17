package mangofusion.apps.shopassist

import com.google.common.truth.Truth.assertThat
import org.junit.Test


class SignUpUtilTest {

    @Test
    fun `empty fields returns false` () {
        val result = SignUpUtil.validateRegistrationInput(
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `empty firstName returns false` () {
        val result = SignUpUtil.validateRegistrationInput(
            "",
            "Rus",
            "ruslaviniumarius@gmail.com",
            "123456",
            "123456",
            "8.9.1999",
            "0749934223",
            "Ineu",
            "Mihail Kogalniceanu, 14"
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `empty lastName returns false` () {
        val result = SignUpUtil.validateRegistrationInput(
            "Laviniu Marius",
            "",
            "ruslaviniumarius@gmail.com",
            "123456",
            "123456",
            "8.9.1999",
            "0749934223",
            "Ineu",
            "Mihail Kogalniceanu, 14"
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `empty email returns false` () {
        val result = SignUpUtil.validateRegistrationInput(
            "Laviniu Marius",
            "Rus",
            "",
            "123456",
            "123456",
            "8.9.1999",
            "0749934223",
            "Ineu",
            "Mihail Kogalniceanu, 14"
        )
        assertThat(result).isFalse()
    }

//    @Test
//    fun `email is not valid returns false` () {
//        val result = SignUpUtil.validateRegistrationInput(
//            "Laviniu Marius",
//            "Rus",
//            "ruslaviniumarius@myemail.com",
//            "123456",
//            "123456",
//            "8.9.1999",
//            "0749934223",
//            "Ineu",
//            "Mihail Kogalniceanu, 14"
//        )
//        //assertThat(result).isFalse()
//        assertThat(result).isTrue()
//    }


    @Test
    fun `empty password returns false` () {
        val result = SignUpUtil.validateRegistrationInput(
            "Laviniu Marius",
            "Rus",
            "ruslaviniumarius@gmail.com",
            "",
            "123456",
            "8.9.1999",
            "0749934223",
            "Ineu",
            "Mihail Kogalniceanu, 14"
        )
        assertThat(result).isFalse()
    }


    @Test
    fun `password contains less than 6 characters returns false` () {
        val result = SignUpUtil.validateRegistrationInput(
            "Laviniu Marius",
            "Rus",
            "ruslaviniumarius@gmail.com",
            "1",
            "1",
            "8.9.1999",
            "0749934223",
            "Ineu",
            "Mihail Kogalniceanu, 14"
        )
        assertThat(result).isFalse()
    }


    @Test
    fun `empty confirmedPassword returns false` () {
        val result = SignUpUtil.validateRegistrationInput(
            "Laviniu Marius",
            "Rus",
            "ruslaviniumarius@gmail.com",
            "123456",
            "",
            "8.9.1999",
            "0749934223",
            "Ineu",
            "Mihail Kogalniceanu, 14"
        )
        assertThat(result).isFalse()
    }

    /*
    @Test
    fun `incorrectly confirmed password returns false` () {
        val result = SignUpUtil.validateRegistrationInput(
            "Laviniu Marius",
            "Rus",
            "ruslaviniumarius@gmail.com",
            "123456",
            "abcdef",
            "8.9.1999",
            "0749934223",
            "Ineu",
            "Mihail Kogalniceanu, 14"
        )
        assertThat(result).isTrue()
    }
*/
    @Test
    fun `empty birthdayDate returns false` () {
        val result = SignUpUtil.validateRegistrationInput(
            "Laviniu Marius",
            "Rus",
            "ruslaviniumarius@gmail.com",
            "123456",
            "123456",
            "",
            "0749934223",
            "Ineu",
            "Mihail Kogalniceanu, 14"
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `empty telephoneNumber returns false` () {
        val result = SignUpUtil.validateRegistrationInput(
            "Laviniu Marius",
            "Rus",
            "ruslaviniumarius@gmail.com",
            "123456",
            "123456",
            "8.9.1999",
            "",
            "Ineu",
            "Mihail Kogalniceanu, 14"
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `empty city returns false` () {
        val result = SignUpUtil.validateRegistrationInput(
            "Laviniu Marius",
            "Rus",
            "ruslaviniumarius@gmail.com",
            "123456",
            "123456",
            "8.9.1999",
            "0749934223",
            "",
            "Mihail Kogalniceanu, 14"
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `empty streetAndNumber returns false` () {
        val result = SignUpUtil.validateRegistrationInput(
            "Laviniu Marius",
            "Rus",
            "ruslaviniumarius@gmail.com",
            "123456",
            "123456",
            "8.9.1999",
            "0749934223",
            "Ineu",
            ""
        )
        assertThat(result).isFalse()
    }
}