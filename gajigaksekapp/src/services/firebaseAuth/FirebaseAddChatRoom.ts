import firestore from '@react-native-firebase/firestore';
import auth from '@react-native-firebase/auth';

class FirebaseAddChatRoom {
  public addNewChatRoom(roomname: string, teamId: string) {
    const currentUser = auth().currentUser?.toJSON();

    firestore()
      .collection('THREADS')
      .doc(teamId)
      .set({
        name: roomname,
        teamId: teamId,
        latestMessage: {
          text: `You have joined the room ${roomname}.`,
          createdAt: new Date().getTime(),
        },
        users: [currentUser?.uid + '|' + currentUser?.email],
      })
      .then(docRef => {
        docRef.collection('MESSAGES').add({
          text: `You have joined the room ${roomname}.`,
          createdAt: new Date().getTime(),
          system: true,
        });
      });
  }

  public async addNewLectureChatRoom(
    roomname: string,
    lectureId: string,
    memberId: string,
    username: string,
    lectureTitle: string,
    userImg: string,
    callback: () => void,
  ) {
    const currentUser = auth().currentUser?.toJSON();
    console.log('ssksk: ', lectureId, lectureId, memberId);
    try {
      await firestore()
        .collection('THREADS')
        .doc(lectureId + '|' + memberId)
        .set({
          name: roomname,
          teamId: lectureId + '|' + memberId,
          lectureId: +lectureId,
          memberId: memberId,
          lectureTitle,
          userImg,
          latestMessage: {
            text: `You have joined the room ${roomname}.`,
            createdAt: new Date().getTime(),
          },
          users: [username],
        })
        .then(docRef => {
          docRef.collection('MESSAGES').add({
            text: `You have joined the room ${roomname}.`,
            createdAt: new Date().getTime(),
            system: true,
          });
        });
      callback();
    } catch (e) {
      callback();
    }
  }
}

export default new FirebaseAddChatRoom();
