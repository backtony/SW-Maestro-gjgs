import {numberJsonType} from '@/utils/Types';
import React from 'react';
import {StyleSheet} from 'react-native';
import {Image, Text, View} from 'react-native';

interface ZoneTitleProps {
  yCord: numberJsonType;
  setYCord: (yCord: numberJsonType) => void;
}

export default class ZoneTitle extends React.Component<ZoneTitleProps, {}> {
  render() {
    return (
      <View
        style={this.zoneTitleStyle.container}
        onLayout={event => {
          const layout = event.nativeEvent.layout;
          let yCord = this.props.yCord;
          yCord['지역별'] = layout.y;
          this.props.setYCord(yCord);
        }}>
        <Image
          style={this.zoneTitleStyle.image}
          source={require('gajigaksekapp/src/asset/iconImage/pin.png')}
        />
        <Text style={this.zoneTitleStyle.text}>지역별</Text>
      </View>
    );
  }

  private zoneTitleStyle = StyleSheet.create({
    container: {
      flexDirection: 'row',
      alignItems: 'center',
      width: '100%',
      marginLeft: 10,
      marginTop: 10,
      marginBottom: 10,
    },
    image: {width: 8, height: 16},
    text: {
      fontSize: 14,
      fontWeight: 'bold',
      color: '#1d1d1f',
      marginTop: 6,
      marginLeft: 4,
    },
  });
}
