import React from 'react';
import {Image, StyleSheet, Text, TouchableOpacity, View} from 'react-native';
import Icon from 'react-native-vector-icons/Ionicons';

interface HomeViewRecommendLectureHeaderProps {
  navigation: any;
  title: string;
  imgUrl: string;
}

export default class HomeViewRecommendLectureHeader extends React.Component<
  HomeViewRecommendLectureHeaderProps,
  {}
> {
  render() {
    return (
      <TouchableOpacity
        style={this.styles.button}
        onPress={() => {
          this.props.navigation.navigate('recommendLecture', {
            title: this.props.title,
          });
        }}>
        <View style={{flexDirection: 'row', alignItems: 'center'}}>
          <Image style={this.styles.img} source={{uri: this.props.imgUrl}} />
          <Text style={this.styles.text}>{this.props.title}</Text>
        </View>
        <Icon name="chevron-forward" size={25} color="#8e8e8f" />
      </TouchableOpacity>
    );
  }

  private styles = StyleSheet.create({
    button: {
      flexDirection: 'row',
      alignItems: 'center',
      justifyContent: 'space-between',
      marginTop: 40,
      marginLeft: 10,
    },
    img: {
      width: 16,
      height: 16,
    },
    text: {
      fontSize: 16,
      lineHeight: 18,
      fontFamily: 'NotoSansCJKkr-Bold',
      marginTop: 5,
      marginLeft: 5,
      //   marginBottom: 10,
      color: '#1d1d1f',
    },
  });
}
