import auth from '@react-native-firebase/auth';

class FirebaseAuth {
  public async signInWithFirebase(id: number) {
    if (!id) {
      alert('로그인을 다시 시도해주세요.');
      return false;
    }

    await auth()
      .createUserWithEmailAndPassword(`${id}@gajigaksek.com`, '123456')
      .then(userCredential => {
        console.log('firebase user: ', userCredential.user);
      })
      .catch(error => {
        console.error('error: ', error);
        return false;
      });

    await auth()
      .signInWithEmailAndPassword(`${id}@gajigaksek.com`, '123456')
      .then(userCredential => {
        console.log('firebase user: ', userCredential.user);
      })
      .catch(error => {
        console.error('error: ', error);
        alert('로그인을 다시 시도해주세요.');
        return false;
      });

    return true;
  }

  public async signOutWithFirebase() {
    await auth()
      .signOut()
      .then(userCredential => {
        console.log('firebase user: ', userCredential.user);
        console.log('firebase logout complete!');
        return true;
      })
      .catch(error => {
        console.error(error);
      });
  }
}

export default new FirebaseAuth();
