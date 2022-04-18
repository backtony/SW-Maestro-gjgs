import firebase from "firebase";
import Cookies from "universal-cookie";

const cookies = new Cookies();

class FirebaseAuth {
  public async signInWithFirebase(id: number) {
    if (!id) {
      alert("로그인을 다시 시도해주세요.");
      return false;
    }

    await firebase
      .auth()
      .createUserWithEmailAndPassword(`${id}@gajigaksek.com`, "123456")
      .then((userCredential: any) => {
        console.log("firebase user: ", userCredential.user);
        cookies.set("firebase_user", JSON.stringify(userCredential.user));
      })
      .catch((error: Error) => {
        console.error("error: ", error);
        return false;
      });

    await firebase
      .auth()
      .signInWithEmailAndPassword(`${id}@gajigaksek.com`, "123456")
      .then((userCredential: any) => {
        console.log("firebase user: ", userCredential.user);
        cookies.set("firebase_user", JSON.stringify(userCredential.user));
      })
      .catch((error: Error) => {
        console.error("error: ", error);
        alert("로그인을 다시 시도해주세요.");
        return false;
      });

    return true;
  }

  public async signOutWithFirebase() {
    await firebase
      .auth()
      .signOut()
      .then((userCredential: any) => {
        console.log("firebase user: ", userCredential.user);
        console.log("firebase logout complete!");
        return true;
      })
      .catch((error: Error) => {
        console.error(error);
      });
  }
}

export default new FirebaseAuth();
