import * as React from 'react';
import {
  StyleSheet,
  Image,
  Text,
  View,
  ScrollView,
  TouchableOpacity,
} from 'react-native';

export default class FirstSubCategoryButton extends React.Component<
  {clicked: boolean; title: string; onPress: () => void},
  {}
> {
  render() {
    return (
      <TouchableOpacity
        style={[
          {
            backgroundColor: '#fafafb',
            width: 162,
            height: 44,
            borderRadius: 8,
            justifyContent: 'center',
            padding: 12,
            marginBottom: 10,
          },
          this.props.clicked ? {backgroundColor: '#4f6cff'} : {},
        ]}
        onPress={() => this.props.onPress()}>
        <Text
          style={[
            {
              fontSize: 12,
              lineHeight: 14,
              fontFamily: 'NotoSansCJKkr-Bold',

              color: '#4a4a4c',
              //   marginLeft: 7,
              marginTop: 3,
              //   marginBottom: 10,
            },
            this.props.clicked ? {color: '#FFF'} : {},
          ]}>
          {this.props.title}
        </Text>
      </TouchableOpacity>
    );
  }
}
