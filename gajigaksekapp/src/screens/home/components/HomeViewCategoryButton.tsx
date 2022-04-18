import React from 'react';
import {Image, StyleSheet, Text, TouchableOpacity, View} from 'react-native';

interface HomeViewCategoryButtonProps {
  navigation: any;
  title: string;
  imgSrc: string;
}

export default class HomeViewCategoryButton extends React.Component<
  HomeViewCategoryButtonProps,
  {}
> {
  render() {
    return (
      <TouchableOpacity
        style={{justifyContent: 'center', alignItems: 'center'}}
        onPress={() => {
          this.props.navigation.navigate('eightCategory', {
            main: this.props.title,
          });
        }}>
        <View style={this.styles.imgWrapper}>
          <Image style={this.styles.img} source={{uri: this.props.imgSrc}} />
        </View>
        <Text>{this.props.title}</Text>
      </TouchableOpacity>
    );
  }

  private styles = StyleSheet.create({
    imgWrapper: {
      width: 68,
      height: 68,
      alignItems: 'center',
      justifyContent: 'center',
      borderRadius: 34,
      backgroundColor: '#fafafb',
      marginBottom: 6,
    },
    img: {
      width: 32,
      height: 32,
    },
  });
}
