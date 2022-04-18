import React from 'react';
import {StyleSheet} from 'react-native';
import {Image, Text, TouchableOpacity, View} from 'react-native';
import Icon from 'react-native-vector-icons/Ionicons';

interface GroupItemContentProps {
  onPress: () => void;
  title: string;
  apply: number;
  all: number;
  subCategory: string;
}

export default class GroupItemContent extends React.Component<
  GroupItemContentProps,
  {}
> {
  render() {
    return (
      <TouchableOpacity
        style={this.styles.container}
        onPress={() => {
          this.props.onPress();
        }}>
        <Image
          style={this.styles.avatar}
          source={{
            uri: 'https://images.unsplash.com/photo-1529156069898-49953e39b3ac?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=2089&q=80',
          }}
        />
        <View>
          <View>
            <Text style={this.styles.titleText} numberOfLines={1}>
              {this.props.title}
            </Text>
            <View style={this.styles.personWrapper}>
              <Icon name="person" size={14} color="#4f6cff" />
              <Text style={this.styles.peopleBold}>{this.props.apply}</Text>
              <Text style={this.styles.people}>/{this.props.all}</Text>
            </View>
            <Text style={this.styles.subCategoryText}>
              {this.props.subCategory}
            </Text>
          </View>
        </View>
      </TouchableOpacity>
    );
  }

  private styles = StyleSheet.create({
    container: {flexDirection: 'row'},
    avatar: {
      width: 60,
      height: 60,
      borderRadius: 63,
      borderWidth: 1,
      borderColor: '#f5f5f7',
      borderStyle: 'solid',
      marginRight: 16,
    },
    titleText: {
      fontSize: 16,
      fontFamily: 'NotoSansCJKkr-Bold',
      lineHeight: 18,
      marginBottom: 6,
      color: '#1d1d1f',
      width: 200,
    },
    subCategoryText: {
      fontSize: 14,
      fontFamily: 'NotoSansCJKkr-Regular',
      lineHeight: 15,
      paddingTop: 4,
      color: '#8e8e8f',
    },
    personWrapper: {
      flexDirection: 'row',
      alignItems: 'center',
      marginBottom: 6,
    },
    people: {
      fontFamily: 'NotoSansCJKkr-Regular',
      fontSize: 14,
      lineHeight: 16,
      color: '#4a4a4c',
    },
    peopleBold: {
      fontFamily: 'NotoSansCJKkr-Bold',
      fontSize: 14,
      lineHeight: 16,
      color: '#4f6cff',
      marginLeft: 5,
    },
  });
}
