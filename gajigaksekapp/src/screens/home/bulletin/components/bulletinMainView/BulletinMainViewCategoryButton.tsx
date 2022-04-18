import React from 'react';
import {Image, StyleSheet, Text, TouchableOpacity} from 'react-native';

interface BulletinMainViewCategoryButtonProps {
  mainCategory: string;
  title: string;
  onPress: (title: string) => void;
}

export default class BulletinMainViewCategoryButton extends React.Component<
  BulletinMainViewCategoryButtonProps,
  {}
> {
  render() {
    return (
      <TouchableOpacity
        style={[
          this.styles.button,
          this.props.mainCategory === this.props.title
            ? {backgroundColor: '#4f6cff'}
            : {},
        ]}
        onPress={() => this.props.onPress(this.props.title)}>
        {this.props.title === '액티비티' && (
          <Image
            style={this.styles.img}
            source={require('gajigaksekapp/src/asset/iconImage/activity.png')}
          />
        )}
        {this.props.title === '쿠킹' && (
          <Image
            style={this.styles.img}
            source={require('gajigaksekapp/src/asset/iconImage/cooking.png')}
          />
        )}
        {this.props.title === '뷰티/헬스' && (
          <Image
            style={this.styles.img}
            source={require('gajigaksekapp/src/asset/iconImage/beautyHealth.png')}
          />
        )}
        {this.props.title === '댄스' && (
          <Image
            style={this.styles.img}
            source={require('gajigaksekapp/src/asset/iconImage/dance.png')}
          />
        )}
        {this.props.title === '미술' && (
          <Image
            style={this.styles.img}
            source={require('gajigaksekapp/src/asset/iconImage/art.png')}
          />
        )}
        {this.props.title === '수공예' && (
          <Image
            style={this.styles.img}
            source={require('gajigaksekapp/src/asset/iconImage/handcraft.png')}
          />
        )}
        {this.props.title === '음악/예술' && (
          <Image
            style={this.styles.img}
            source={require('gajigaksekapp/src/asset/iconImage/music.png')}
          />
        )}
        <Text
          style={[
            this.styles.text,
            this.props.mainCategory === this.props.title ? {color: '#FFF'} : {},
          ]}>
          {this.props.title}
        </Text>
      </TouchableOpacity>
    );
  }

  private styles = StyleSheet.create({
    button: {
      backgroundColor: '#f5f5f7',
      padding: 10,
      borderRadius: 6,
      flexDirection: 'row',
      marginRight: 10,
      alignItems: 'center',
    },
    img: {width: 16, height: 16},
    text: {
      fontFamily: 'NotoSansCJKkr-Regular',
      fontSize: 12,
      lineHeight: 14,
      color: '#4a4a4c',
      marginLeft: 6,
    },
  });
}
