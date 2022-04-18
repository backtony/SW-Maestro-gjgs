import React from 'react';
import {
  StyleSheet,
  Image,
  Text,
  View,
  Modal,
  TouchableOpacity,
  Share,
} from 'react-native';
import Icon from 'react-native-vector-icons/Ionicons';
import Clipboard from '@react-native-clipboard/clipboard';

export default class ShareModal extends React.Component<
  {
    text: string;
    url: string;
    modalVisible: boolean;
    setModalVisible: (visible: boolean) => void;
    setParentSubZone: (sub: string) => void;
  },
  {}
> {
  private async copyToClipboard(text: string) {
    Clipboard.setString(text);
    alert('주소가 복사되었습니다.');
  }

  private async onShare(text: string) {
    try {
      const result = await Share.share({
        message: text,
      });

      if (result.action === Share.sharedAction) {
        if (result.activityType) {
          // shared with activity type of result.activityType
        } else {
          // shared
        }
      } else if (result.action === Share.dismissedAction) {
        // dismissed
      }
    } catch (error) {
      alert('공유에 실패했습니다.');
    }
  }
  render() {
    return (
      <Modal
        animationType="fade"
        transparent={true}
        visible={this.props.modalVisible}
        onRequestClose={() => {
          this.props.setModalVisible(false);
        }}>
        <View style={this.styles.centeredView}>
          <View style={this.styles.modalView}>
            <View style={this.styles.imgWrapper}>
              <Image
                style={{width: 48, height: 48}}
                source={require('gajigaksekapp/src/asset/iconImage/bow.png')}
              />
            </View>
            <Text style={this.styles.text1}>
              {`친구에게 ${this.props.text}`}
            </Text>
            <Text style={this.styles.text2}>공유하세요</Text>
            <TouchableOpacity
              style={this.styles.button1}
              onPress={() => this.onShare(this.props.url)}>
              <Image
                style={{marginRight: 10}}
                source={require('gajigaksekapp/src/asset/iconImage/group.png')}
              />
              <Text style={this.styles.buttonText1}>카카오톡으로 공유</Text>
            </TouchableOpacity>
            <TouchableOpacity
              style={this.styles.button2}
              onPress={() => this.copyToClipboard(this.props.url)}>
              <Icon
                name="copy-outline"
                size={20}
                color="white"
                style={{marginRight: 10}}
              />
              <Text style={this.styles.buttonText2}>링크 복사</Text>
            </TouchableOpacity>
            <TouchableOpacity
              style={this.styles.button3}
              onPress={() => {
                this.props.setModalVisible(false);
              }}>
              <Text style={this.styles.buttonText3}>취소</Text>
            </TouchableOpacity>
          </View>
        </View>
      </Modal>
    );
  }

  private styles = StyleSheet.create({
    centeredView: {
      flex: 13,
      justifyContent: 'center',
      alignItems: 'center',
      width: '100%',
      backgroundColor: 'rgba(52, 52, 52, 0.8)',
    },
    modalView: {
      alignItems: 'center',

      backgroundColor: '#FFF',
      borderRadius: 20,
      padding: 20,
      shadowColor: '#000',
      shadowOffset: {
        width: 0,
        height: 2,
      },
      shadowOpacity: 0.25,
      shadowRadius: 4,
      elevation: 5,
    },
    imgWrapper: {
      backgroundColor: '#f5f5f7',
      padding: 16,
      borderRadius: 100,
      width: 80,
      height: 80,
    },
    text1: {
      fontSize: 20,
      lineHeight: 24,
      fontFamily: 'NotoSansCJKkr-Bold',
      marginTop: 20,
    },
    text2: {
      fontSize: 20,
      lineHeight: 24,
      fontFamily: 'NotoSansCJKkr-Bold',
    },
    button1: {
      flexDirection: 'row',
      backgroundColor: '#fee500',
      padding: 12,
      width: 240,
      borderRadius: 100,
      alignItems: 'center',
      justifyContent: 'center',
      marginTop: 40,
    },
    buttonText1: {
      fontSize: 16,
      lineHeight: 20,
      fontFamily: 'NotoSansCJKkr-Bold',
    },
    button2: {
      flexDirection: 'row',
      backgroundColor: '#4f6cff',
      padding: 12,
      width: 240,
      borderRadius: 100,
      alignItems: 'center',
      justifyContent: 'center',
      marginTop: 12,

      shadowColor: '#4c4f6cff',
      shadowOffset: {
        width: 0,
        height: 4,
      },
      shadowOpacity: 0.29,
      shadowRadius: 4.65,

      elevation: 7,
    },
    buttonText2: {
      fontSize: 16,
      lineHeight: 20,
      fontFamily: 'NotoSansCJKkr-Bold',
      color: '#FFF',
    },
    button3: {
      flexDirection: 'row',

      padding: 12,
      width: 240,
      borderRadius: 100,
      alignItems: 'center',
      justifyContent: 'center',
      marginTop: 40,
    },
    buttonText3: {
      fontSize: 16,
      lineHeight: 20,
      fontFamily: 'NotoSansCJKkr-Bold',
      color: '#8e8e8f',
    },
  });
}
