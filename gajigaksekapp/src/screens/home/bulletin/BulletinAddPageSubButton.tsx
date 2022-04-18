import * as React from 'react';
import {Text, View, FlatList, TouchableOpacity} from 'react-native';

export default class BulletinAddPageSubButton extends React.Component<
  {title: string; clicked: boolean; onClick: () => void},
  {}
> {
  render() {
    return (
      <TouchableOpacity
        style={[
          {
            backgroundColor: 'rgba(245, 245, 247, 0.6)',
            justifyContent: 'center',
            alignItems: 'center',
            borderRadius: 6,
            marginRight: 10,
            marginTop: 10,
            padding: 10,
          },
          this.props.clicked ? {backgroundColor: '#4f6cff'} : {},
        ]}
        onPress={() => {
          this.props.onClick();
        }}>
        <Text
          style={[
            {
              fontSize: 14,
              lineHeight: 16,
              fontFamily: 'NotoSansCJKkr-Regular',
              //   marginLeft: 20,
              marginTop: 3,
              //   marginBottom: 10,
              color: '#1d1d1f',
            },
            this.props.clicked ? {color: '#FFF'} : {},
          ]}>
          {this.props.title}
        </Text>
      </TouchableOpacity>
    );
  }
}
