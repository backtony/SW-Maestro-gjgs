import * as React from 'react';
import {Text, View, TouchableOpacity, StyleSheet} from 'react-native';
import Icon from 'react-native-vector-icons/Ionicons';
import CheckBox from '@react-native-community/checkbox';

export default class FavoriteSelectModalGroupBlock extends React.Component<
  {
    group: any;
    checked: boolean;
    onChange: (id: number) => void;
  },
  {}
> {
  render() {
    return (
      <TouchableOpacity style={this.styles.button} onPress={() => {}}>
        <View style={{flexDirection: 'row', alignItems: 'center'}}>
          <CheckBox
            disabled={false}
            value={this.props.checked}
            onValueChange={() => this.props.onChange(this.props.group.id)}
            style={{marginRight: 10, marginLeft: 5}}
          />
          <View style={this.styles.personWrapper}>
            <Icon
              name={
                this.props.group.id === -1 ? 'md-person-sharp' : 'ios-people'
              }
              size={24}
              color="#8e8e8f"
              style={{}}
            />
          </View>
          <View>
            <View style={{flexDirection: 'row'}}>
              <Text style={this.styles.text}>{this.props.group.title}</Text>
            </View>
          </View>
        </View>
      </TouchableOpacity>
    );
  }

  private styles = StyleSheet.create({
    button: {
      width: '100%',
      flexDirection: 'row',
      alignItems: 'center',
      justifyContent: 'space-between',
      paddingVertical: 17,
    },
    personWrapper: {
      width: 44,
      height: 44,
      borderRadius: 63,
      borderWidth: 1,
      borderColor: '#f5f5f7',
      borderStyle: 'solid',
      marginHorizontal: 10,
      backgroundColor: '#f5f5f7',
      alignItems: 'center',
      justifyContent: 'center',
    },
    text: {
      fontSize: 16,
      fontFamily: 'NotoSansCJKkr-Regular',
      lineHeight: 18,
    },
  });
}
