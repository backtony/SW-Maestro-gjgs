import * as React from 'react';
import {Text, View, Image, TouchableOpacity, StyleSheet} from 'react-native';

interface LectureDetailDirectorProfileBlockProps {
  profile: any;
  navigation: any;
}

export default class LectureDetailDirectorProfileBlock extends React.Component<
  LectureDetailDirectorProfileBlockProps,
  {}
> {
  render() {
    return (
      <TouchableOpacity
        style={[this.styles.button]}
        onPress={() => {
          this.props.navigation.navigate('directorProfile', {
            info: this.props.profile.info,
            directorId: this.props.profile.directorId,
            nickname: this.props.profile.nickname,
            image: this.props.profile.image,
          });
        }}>
        <View style={{flexDirection: 'row', alignItems: 'center'}}>
          <Image
            style={this.styles.image}
            source={{
              uri: this.props.profile.image,
            }}
          />
          <View style={{justifyContent: 'center'}}>
            <View style={{flexDirection: 'row'}}>
              <Text style={this.styles.nickname}>
                {this.props.profile.nickname}
              </Text>
            </View>
            <Text numberOfLines={2} style={this.styles.info}>
              {this.props.profile.info}
            </Text>
          </View>
        </View>
      </TouchableOpacity>
    );
  }

  private styles = StyleSheet.create({
    button: {
      backgroundColor: 'transparent',
      width: '100%',
      borderRadius: 6,
      flexDirection: 'row',
      alignItems: 'center',
      justifyContent: 'space-between',
      paddingVertical: 17,
      paddingHorizontal: 20,
    },
    image: {
      width: 60,
      height: 60,
      borderRadius: 63,
      borderWidth: 1,
      borderColor: '#f5f5f7',
      borderStyle: 'solid',
      // marginHorizontal: 20,
      marginRight: 10,
    },
    nickname: {
      fontSize: 20,
      fontFamily: 'NotoSansCJKkr-Bold',
      lineHeight: 24,
      color: '#FFF',
    },
    info: {
      fontSize: 12,
      fontFamily: 'NotoSansCJKkr-Regular',
      lineHeight: 14,
      paddingTop: 4,
      color: '#B5C4FF',
      width: '60%',
    },
  });
}
