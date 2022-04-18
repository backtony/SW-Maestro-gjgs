import React from 'react';
import {StyleSheet, Text, TouchableOpacity, View} from 'react-native';
import {Button} from 'react-native-elements';
import Icon from 'react-native-vector-icons/Ionicons';

interface BulletinDetailViewBottomProps {
  myFavorite: boolean;
  postFavoriteBulletin: () => void;
  deleteFavoriteBulletin: () => void;
  postTeamApply: () => void;
}

export default class BulletinDetailViewBottom extends React.Component<
  BulletinDetailViewBottomProps,
  {}
> {
  render() {
    return (
      <View style={this.styles.container}>
        <Button
          icon={
            this.props.myFavorite ? (
              <Icon name="heart" size={30} color="#ff4f4f" />
            ) : (
              <Icon name="heart-outline" size={30} color="grey" />
            )
          }
          buttonStyle={this.styles.button1}
          onPress={() => {
            if (!this.props.myFavorite) {
              this.props.postFavoriteBulletin();
            } else if (this.props.myFavorite) {
              this.props.deleteFavoriteBulletin();
            }
          }}
        />
        <TouchableOpacity
          style={this.styles.button2}
          onPress={() => {
            this.props.postTeamApply();
          }}>
          <Text style={this.styles.text}>참가 신청</Text>
        </TouchableOpacity>
      </View>
    );
  }

  private styles = StyleSheet.create({
    container: {
      height: 60,
      marginHorizontal: 16,
      paddingVertical: 8,
      marginVertical: 8,
      flexDirection: 'row',
    },
    button1: {
      backgroundColor: 'transparent',
    },
    button2: {
      backgroundColor: '#4f6cff',
      height: 44,
      alignItems: 'center',
      justifyContent: 'center',
      borderRadius: 6,
      marginLeft: 5,
      flex: 1,
    },
    text: {
      fontFamily: 'NotoSansCJKkr-Bold',
      fontSize: 16,
      color: '#FFF',
    },
  });
}
