import React from 'react';
import {TouchableOpacity, View, Text, Image, StyleSheet} from 'react-native';

interface HomeViewAdBoxProps {
  color: string;
}

export default class HomeViewAdBox extends React.Component<
  HomeViewAdBoxProps,
  {}
> {
  render() {
    return (
      <TouchableOpacity
        style={[
          {
            backgroundColor: `${this.props.color}`,
          },
          this.styles.button,
        ]}>
        <View style={{paddingLeft: 20}}>
          <Text style={this.styles.text}>친구따라 왔다</Text>
          <Text style={this.styles.text2}>할인받고 간다!</Text>
        </View>
        <Image
          style={this.styles.img}
          source={require('gajigaksekapp/src/asset/iconImage/main1.png')}
        />
      </TouchableOpacity>
    );
  }

  private styles = StyleSheet.create({
    button: {
      height: 140,
      width: 320,
      borderRadius: 20,
      alignItems: 'center',
      justifyContent: 'space-between',
      flexDirection: 'row',
      marginRight: 10,
    },
    text: {
      fontSize: 16,
      lineHeight: 18,
      fontFamily: 'NotoSansCJKkr-Regular',
      color: '#FFF',
    },
    text2: {
      fontSize: 24,
      lineHeight: 28,
      fontFamily: 'NotoSansCJKkr-Bold',
      color: '#FFF',
    },
    img: {width: 175, height: 120, position: 'absolute', right: 0},
  });
}
